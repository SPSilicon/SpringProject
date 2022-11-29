const videoElem = document.getElementById('video');
const startbutton = document.getElementById('start');
const streamStart = document.getElementById('sStart');
const audioSelect = document.querySelector('select#aud');
const videoSelect = document.querySelector('select#vid');
const streamStatus = document.querySelector('label#streamStatus');
const recordRow = document.querySelector("div#recordRow");
var sock;


function capture() {
  var checked = document.querySelector('input[name="captureMethod"]:checked').value;

  if(checked=='scrn') {
    startCapture();
  } else if(checked=='dvc') {
    init();
  }
}

async function startCapture() {
  if (window.stream) {
    window.stream.getTracks().forEach(track => {
      track.stop();
    });
  }
  let captureStream = null;
  const gdmOptions = {
    video: {
      frameRate: { ideal: 60, max: 60 },
      cursor: "always",
      width: { ideal: 1920 },
      height: { ideal: 1080 },
    },
    audio: {
      echoCancellation: false,
      noiseSuppression: false,
      sampleRate: 44100
    }
  }  
  try {
    await navigator.mediaDevices.getDisplayMedia(gdmOptions).then(gotStream);
    dumpOptionsInfo();
  } catch (err) {
    console.error(`Error: ${err}`);
  }
}



streamStart.onclick = function (event) {

  if(sock == null ||sock.readyState == WebSocket.CLOSED) {
    var streamName = document.getElementById('sName').value;
    conn("wss://spsi.kro.kr/stream/share/"+streamName);
  } else if(sock.readyState == WebSocket.OPEN) {
    sock.close();
  }
};

function conn(serverURL) {
  try{
    sock = new WebSocket(serverURL);
    sock.addEventListener('open',function (event) {
      console.log("연결됨 :",event);
      streamStatus.textContent="연결됨"
    });

    sock.onclose = function() {
        console.log("closed");
        streamStatus.textContent="연결 끊어짐"
    };
  } catch(e){
    streamStatus.textContent="연결 실패"+e;

    console.error(`에러 : ${e}`);
  }

}
  


startbutton.onclick=startRecord;
function startRecord() {
  if(videoElem.srcObject == null) {
    alert("캡쳐가 필요합니다.");
    return;
  } else if(sock == null||sock.readyState == WebSocket.CLOSED) {
    alert("스트림 연결이 필요합니다.");
    return;
  } 
  const stream = videoElem.srcObject;
  const options = { 
                    audioBitsPerSecond : 128000,
                    videoBitsPerSecond : 13000000,
                    mimeType: 'video/x-matroska;codecs=h264'
  };
    const mediaRecorder = new MediaRecorder(stream, options);
    
    handleDataAvailable = (event)=> {
      if(event.data.size > 0) {
          //console.log(event.data);
          if(sock.readyState==WebSocket.OPEN){
            try {
              recordRow.lastChild.nodeValue = event.data.size+"바이트 전송";
              sock.send(event.data);
            } catch(e) {
              console.log("에러!",e);
            }
          }
      }
    };

    mediaRecorder.ondataavailable = handleDataAvailable;
    mediaRecorder.start(1);

    startbutton.innerHTML="송출중";
    const streamName = document.getElementById('sName').value;
    recordRow.appendChild(document.createTextNode("spsi.kro.kr/stream/play/"+streamName));
    recordRow.appendChild(document.createTextNode("전송중 ..."));

    startbutton.onclick = (e)=>{
      console.log("stopping");
      mediaRecorder.stop();
      sock.close();
      recordRow.removeChild(recordRow.lastChild);
      recordRow.removeChild(recordRow.lastChild);
      startbutton.onclick=startRecord;
      startbutton.innerHTML="송출시작하기";
    };
}
function dumpOptionsInfo() {
    const videoTrack = videoElem.srcObject.getVideoTracks()[0];
  
    console.info("Track settings:");
    console.info(JSON.stringify(videoTrack.getSettings(), null, 2));
    console.info("Track constraints:");
    console.info(JSON.stringify(videoTrack.getConstraints(), null, 2));
  }



  function gotStream(stream) {
    window.stream = stream; // make stream available to console
    videoElem.srcObject = stream;
    // Refresh button list in case labels have become available
    return navigator.mediaDevices.enumerateDevices();
  }

  const selectors = [audioSelect, videoSelect];
  function gotDevices(deviceInfos) {
    // Handles being called several times to update labels. Preserve values.
    const values = selectors.map(select => select.value);
    selectors.forEach(select => {
      while (select.firstChild) {
        select.removeChild(select.firstChild);
      }
    });
    for (let i = 0; i !== deviceInfos.length; ++i) {
      const deviceInfo = deviceInfos[i];
      const option = document.createElement('option');
      option.value = deviceInfo.deviceId;
      if (deviceInfo.kind === 'audioinput') {
        option.text = deviceInfo.label || `microphone ${audioSelect.length + 1}`;
        audioSelect.appendChild(option);
      } else if (deviceInfo.kind === 'videoinput') {
        option.text = deviceInfo.label || `camera ${videoSelect.length + 1}`;
        videoSelect.appendChild(option);
      } else {
        console.log('Some other kind of source/device: ', deviceInfo);
      }
    }
    
    selectors.forEach((select, selectorIndex) => {
      if (Array.prototype.slice.call(select.childNodes).some(n => n.value === values[selectorIndex])) {
        select.value = values[selectorIndex];
      }
    });
  }
  navigator.mediaDevices.enumerateDevices().then(gotDevices).catch(handleError);


  function init() {
    if (window.stream) {
      window.stream.getTracks().forEach(track => {
        track.stop();
      });
    }
    const audioSource = audioSelect.value;
    const videoSource = videoSelect.value;
    const constraints = {
      audio: {noiseSuppression: false, deviceId: audioSource ? {exact: audioSource} : undefined},
      video: { width: { ideal: 1920 }, height: { ideal: 1080 }, deviceId: videoSource ? {exact: videoSource} : undefined}
    };
    navigator.mediaDevices.getUserMedia(constraints).then(gotStream).then(gotDevices).catch(handleError);
  }
  function handleError(error) {
    console.log('navigator.MediaDevices.getUserMedia error: ', error.message, error.name);
  }

  audioSelect.onchange = init;
  videoSelect.onchange = init;
  init();
 