                var url ="/kayttajat/" +message+ "/kommentit";
                var http = new XMLHttpRequest();
                http.onreadystatechange = function() {
                    if (this.readyState != 4) {
                    console.log(url)

                        return
                    }
                   
                }

                function addComment() {
                    var post =  document.getElementById("commentField").value;
                    console.log(post);
                    console.log("addComment function called");
                    http.open("POST", url,false);
                    http.send(post);
                    $('#commentField').val('');
                    loadComments(); 
                    
                    
                }
               function loadComments(){
               console.log("loadcomments kutsuttu");
                $('#fragmentcomments').load(url);
   
               }
                
                function addReply(messageId) {
                    console.log(messageId);
                    var palli ="/kayttajat/" +message+ "/kommentit/" +messageId+ "/reply";
                    var reply =  document.getElementById("replyField-"+messageId).value;
                    console.log(reply);
                    console.log("addReply function called,, messageID: "+messageId);
                    http.open("POST", palli,false);
                    http.send(reply);
                    $('#replyField').val('');
                    loadReply(messageId); 
                    
                    
                    }
               function loadReply(messageId){
                    var palli ="/kayttajat/" +message+ "/kommentit/" +messageId+ "/reply";
               console.log("loadREply kutsuttu");
                $('#fragmentcomments').load(url);
   
               }
            
            

                                    

                                     
