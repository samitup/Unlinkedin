                console.log("Button clicked");
                var url ="/kayttajat/" +message+ "/profiilikuva";
                var http = new XMLHttpRequest();
                http.onreadystatechange = function() {
                    if (this.readyState != 4) {
                    console.log(url)

                        return
                    }
                    console.log(url)
                }
                function addProfilePic() {
                    var data =  document.getElementById("singleImage").src;
                    console.log("addProfilePic function called");
                    http.open("POST", url)
                    http.send(JSON.stringify(data))

                }
            

                                    

                                     
