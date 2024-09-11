let songIndex = 0;
let audioElement = new Audio('songs/sound1.mp3');
let masterPlay = document.getElementById('masterPlay');
let myProgressBar = document.getElementById('myProgressBar');
let gif = document.getElementById('gif');
let masterSongName = document.getElementById('masterSongName');
let songItems = Array.from(document.getElementsByClassName('songItem'));

let songs = [
  {songName: "Janji-Heroes-Tonight-feat-Johnning-NCS-Release", filePath: "songs/sound1.mp3", coverPath: "covers/img1.jpg"},  
  {songName: "Sakhiyaan - Salam-e-Ishq", filePath: "songs/sound2.mp3", coverPath: "covers/img2.jpg"},
  {songName: "Cielo - Huma-Huma", filePath: "songs/sound3.mp3", coverPath: "covers/img3.jpg"},
  {songName: "DEAF KEV - Invincible [NCS Release]", filePath: "songs/sound4.mp3", coverPath: "covers/img4.jpg"},
  {songName: "Warriyo - Mortals [NCS Release]", filePath: "songs/sound5.mp3", coverPath: "covers/img5.jpg"},
  {songName: "Bhula Dena - Salam-e-Ishq", filePath: "songs/sound6.mp3", coverPath: "covers/img6.jpg"},
  {songName: "Different Heaven & EH!DE - My Heart [NCS Release]", filePath: "songs/sound7.mp3", coverPath: "covers/img7.jpg"},
  {songName: "Rabba - Salam-e-Ishq", filePath: "songs/sound8.mp3", coverPath: "covers/img8.jpg"},
  {songName: "Na Jaana - Salam-e-Ishq", filePath: "songs/sound9.mp3", coverPath: "covers/img9.jpg"},
  {songName: "Tumhari Kasam - Salam-e-Ishq", filePath: "songs/sound10.mp3", coverPath: "covers/img10.jpg"},
];

songItems.forEach((element, i)=>{ 
    element.getElementsByTagName("img")[0].src = songs[i].coverPath; 
    element.getElementsByClassName("songName")[0].innerText = songs[i].songName; 
});
 
masterPlay.addEventListener('click', ()=>{
    if(audioElement.paused || audioElement.currentTime<=0){
        audioElement.play();
        masterPlay.classList.remove('fa-play-circle');
        masterPlay.classList.add('fa-pause-circle');
        gif.style.opacity = 1;
    }
    else{
        audioElement.pause();
        masterPlay.classList.remove('fa-pause-circle');
        masterPlay.classList.add('fa-play-circle');
        gif.style.opacity = 0;
    }
});

audioElement.addEventListener('timeupdate', ()=>{ 
    progress = parseInt((audioElement.currentTime/audioElement.duration)* 100); 
    myProgressBar.value = progress;
});

myProgressBar.addEventListener('change', ()=>{
    audioElement.currentTime = myProgressBar.value * audioElement.duration/100;
});

const makeAllPlays = ()=>{
    Array.from(document.getElementsByClassName('songItemPlay')).forEach((element)=>{
        element.classList.remove('fa-pause-circle');
        element.classList.add('fa-play-circle');
    });
};

Array.from(document.getElementsByClassName('songItemPlay')).forEach((element)=>{
    element.addEventListener('click', (e)=>{ 
        makeAllPlays();
        songIndex = parseInt(e.target.id);
        e.target.classList.remove('fa-play-circle');
        e.target.classList.add('fa-pause-circle');
        audioElement.src = `songs/sound${songIndex+1}.mp3`;
        masterSongName.innerText = songs[songIndex].songName;
        audioElement.currentTime = 0;
        audioElement.play();
        gif.style.opacity = 1;
        masterPlay.classList.remove('fa-play-circle');
        masterPlay.classList.add('fa-pause-circle');
    });
});

document.getElementById('next').addEventListener('click', ()=>{
    if(songIndex>=9){
        songIndex = 0;
    }
    else{
        songIndex += 1;
    }
    audioElement.src = `songs/sound${songIndex+1}.mp3`;
    masterSongName.innerText = songs[songIndex].songName;
    audioElement.currentTime = 0;
    audioElement.play();
    masterPlay.classList.remove('fa-play-circle');
    masterPlay.classList.add('fa-pause-circle');
});

document.getElementById('previous').addEventListener('click', ()=>{
    if(songIndex<=0){
        songIndex = 0;
    }
    else{
        songIndex -= 1;
    }
    audioElement.src = `songs/sound${songIndex+1}.mp3`;
    masterSongName.innerText = songs[songIndex].songName;
    audioElement.currentTime = 0;
    audioElement.play();
    masterPlay.classList.remove('fa-play-circle');
    masterPlay.classList.add('fa-pause-circle');
});

// Event listener for when the current song ends
audioElement.addEventListener('ended', () => {
    // Increment song index to play the next song
    songIndex = (songIndex + 1) % songs.length;
    audioElement.src = songs[songIndex].filePath;
    masterSongName.innerText = songs[songIndex].songName;
    audioElement.currentTime = 0;
    audioElement.play();
    masterPlay.classList.remove('fa-play-circle');
    masterPlay.classList.add('fa-pause-circle');
});
