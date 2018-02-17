"use strict";

function ajax(method, destination, callback, errorCallback, sendData) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4) {
            if (this.status == 200) {
                if (callback != null) {
                    callback(this.responseText);
                }
            } else {
                console.error("AJAX response error: " + this.status);
                if (errorCallback != null) {
                    errorCallback(this.responseText);
                }
            }
        }
    };
    xhttp.open(method, destination, true);
    if (method == "POST") {
        xhttp.setRequestHeader("Content-type", "application/json; charset=utf-8");
        xhttp.send(JSON.stringify(sendData));
    } else {
        xhttp.send();
    }
}

function getStatus(callback) {
    ajax("GET", "/status", callback);
}

function refreshAll() {
    getStatus(function(newStatus) {
        try {
            var parsedStatus = JSON.parse(newStatus);

            var t =  new Date(parsedStatus.timestamp);
            document.getElementById("timestamp").innerHTML = pad(t.getHours()) + ":" + pad(t.getMinutes()) + ":" + pad(t.getSeconds());
            document.getElementById("temp").innerHTML = twoDigits(parsedStatus.temp);
            document.getElementById("requiredTemp").innerHTML = twoDigits(parsedStatus.requiredTemp);
            if (parsedStatus.heating == true) {
                document.getElementById("on").style.display = "block";
                document.getElementById("off").style.display = "none";
            } else {
                document.getElementById("on").style.display = "none";
                document.getElementById("off").style.display = "block";
            }
        } catch (err) {
            console.error(err);
            document.getElementById("temp").innerHTML = "error";
            document.getElementById("requiredTemp").innerHTML = "???";
            document.getElementById("on").style.display = "none";
            document.getElementById("off").style.display = "none";
        }
    });
}

function refreshTempPeriodically() {
    refreshAll();
    setTimeout(refreshTempPeriodically, 1000)
}

refreshTempPeriodically();

function pad(d) {
    return (d < 10) ? '0' + d.toString() : d.toString();
}

function twoDigits(num) {
    return parseFloat(Math.round(num * 100) / 100).toFixed(2);
}

//send new required temp
function setNewTemp() {
    var postData = {};
    postData.temp = document.getElementById("setTemp").value;
    console.info("setTemp = " + postData.temp);

    var callback = function(){
        var node = document.getElementById("setTempError");
        while (node.hasChildNodes()) {
            node.removeChild(node.firstChild);
        }
    };

    var errorCallback = function(err){
        document.getElementById("setTempError").innerHTML = JSON.parse(err).errors[0].defaultMessage;
    };
    ajax("POST", "/temp", callback, errorCallback, postData);
}
