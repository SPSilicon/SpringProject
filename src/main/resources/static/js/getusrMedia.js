// Put variables in global scope to make them available to the browser console.

const videoElem = document.getElementById('video');
const stopbutton = document.getElementById('stop');
const startbutton = document.getElementById('start');
const streamName = document.getElementById('sName');
const streamStart = document.getElementById('sStart');
var sock;

async function startCapture() {
    let captureStream = null;
    const gdmOptions = {
      video: {
        cursor: "always"
      },
      audio: {
        echoCancellation: false,
        noiseSuppression: false,
        sampleRate: 44100
      }
    }  
    try {
        videoElem.srcObject = await navigator.mediaDevices.getDisplayMedia(gdmOptions);
        dumpOptionsInfo();
    } catch (err) {
      console.error(`Error: ${err}`);
    }
}

streamStart.addEventListener('click',function (event) {
  conn("wss://spsi.kro.kr/stream");
});

function conn(serverURL) {
  
  try{
    sock = new WebSocket(serverURL);
    
    sock.addEventListener('open',function (event) {
      console.log("연결됨 :",event);
    });
  } catch(e){
    console.error(`에러 : ${e}`);
  }

}

function send(blob) {
  try {

    sock.send(blob);
    
  } catch(e) {
    console.log("에러!",e);
  }

}


function download(data) {
    const chunks = [data];
    const blob = new Blob(chunks, {
      type: "video/webm"
    });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    document.body.appendChild(a);
    a.style = "display: none";
    a.href = url;
    a.download = "test.webm";
    a.click();
    window.URL.revokeObjectURL(url);
  }

  
stopbutton.onclick=startRecord;
function startRecord() {
    const stream = videoElem.captureStream(60);
    const options = { 
                      audioBitsPerSecond : 128000,
                      videoBitsPerSecond : 2500000,
                      mimeType: 'video/webm;'
  };
    const mediaRecorder = new MediaRecorder(stream, options);
    
    //const recordedChunks = [];
    handleDataAvailable = (event)=> {
      
        console.log("data-available");
      if(event.data.size > 0) {
          //recordedChunks.push(event.data);
          //console.log(recordedChunks);
          console.log(event.data);
          //download(event.data);
          if(conn) send(event.data);
      }
    };

    mediaRecorder.ondataavailable = handleDataAvailable;
    mediaRecorder.start(10);
    stopbutton.innerHTML="녹화중";

    stopbutton.onclick = (e)=>{
        console.log("stopping");
        mediaRecorder.stop();
        stopbutton.onclick=startRecord;
        stopbutton.innerHTML="녹화시작하기";
    };
}


function dumpOptionsInfo() {
    const videoTrack = videoElem.srcObject.getVideoTracks()[0];
  
    console.info("Track settings:");
    console.info(JSON.stringify(videoTrack.getSettings(), null, 2));
    console.info("Track constraints:");
    console.info(JSON.stringify(videoTrack.getConstraints(), null, 2));
  }

  
 