// Put variables in global scope to make them available to the browser console.
const videoElem = document.getElementById('video');
const stopbutton = document.getElementById('stop');
const startbutton = document.getElementById('start');
const gdmOptions = {
    video: {
      cursor: "always"
    },
    audio: {
      echoCancellation: true,
      noiseSuppression: true,
      sampleRate: 44100
    }
  }

async function startCapture() {
    let captureStream = null;
  
    try {
        videoElem.srcObject = await navigator.mediaDevices.getDisplayMedia(gdmOptions);
        dumpOptionsInfo();
    } catch (err) {
      console.error(`Error: ${err}`);
    }
}

stopbutton.onclick=startRecord;

function download(chunks) {
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


function startRecord() {
    const stream = videoElem.captureStream(30);
    const options = { mimeType: "video/webm;" };
    const mediaRecorder = new MediaRecorder(stream, options);
    const recordedChunks = [];

    handleDataAvailable = (event)=> {
        console.log("data-available");
        if(event.data.size > 0) {
            recordedChunks.push(event.data);
            console.log(recordedChunks);
            download(recordedChunks);
        }
    };

    mediaRecorder.ondataavailable = handleDataAvailable;
    mediaRecorder.start();
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

  
 