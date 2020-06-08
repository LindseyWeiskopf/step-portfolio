// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


function getComments() {

  document.getElementById('greeting-container').innerHTML = "";
  var commentNum = parseInt(document.getElementById('quantity').value);
  
  fetch('/comments?quantity=' + commentNum).then(response => response.json()).then((posts) => {
      const liElement = document.getElementById('greeting-container');
      posts.forEach((post) => {
        liElement.appendChild(createPostElement(post));
    })
  }); 
}

function createPostElement(post) {
  const postElement = document.createElement('li');
  postElement.className = 'post';

  const commentElement = document.createElement('span');
  commentElement.innerText = post.comment; 
  
  postElement.appendChild(commentElement);
  return postElement;
}

function deleteComments() {
  const request = new Request('/delete-comment', {method: 'POST'});
  fetch(request).then(response => getComments());
}

function addRandomGreeting() {
  const greetings = 
     ['Hey, How are you?', 'Good Morning, friend!', 'Howdy, yall', 'Today\'s going to be the best day ever'];
  // Pick a random fact
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];
  // Add it to the page
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

// Global variables that apply across functions
var map, heatmap;
var heatmapData = [];

// Creates a map and heatmap layer
function initMap() {
  // Coordinates centers map  around NYC
  var myLatlng = {lat: 40.763232, lng: -73.951047};
  var mapOptions = {
    center: myLatlng, 
    zoom: 14,
    mapTypeControl: false 
  };
  map = new google.maps.Map(document.getElementById('map'), mapOptions);
  getCoords();
  createHeatmap();
  
}

// Iterates through coordinates and delivers to other functions
function getCoords() {
  var image = 'http://maps.google.com/mapfiles/kml/pal4/icon47.png';
  fetch('/dropoff-data').then(response => response.json()).then((dropoffs) => {

    dropoffs.forEach((dropoff) => {
      createMarkers(dropoff, image);
      getHeatmapData(dropoff);
    });
  });
}

function createMarkers(dropoff, image) {
  var marker = new google.maps.Marker({
    position: {lat: dropoff.lat, lng: dropoff.lng}, 
    map: map,
    icon: image
  });
  marker.setMap(map);
  google.maps.event.trigger(map, 'resize');
}

function getHeatmapData(dropoff) {
  var latLng = new google.maps.LatLng(dropoff.lat, dropoff.lng);
  heatmapData.push(latLng);
}

function createHeatmap() {
  heatmap = new google.maps.visualization.HeatmapLayer({
    data: heatmapData,
    dissipating: false,
    map: map,
    radius: 1,
    opacity: 0.5,
    // green gradient
    gradient:[ '#ffffff','#ffff99','#ccff66','#66ff33','#33cc33','#009933']
  });
}