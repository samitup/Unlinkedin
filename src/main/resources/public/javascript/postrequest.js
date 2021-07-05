                var url = encodeURI("/kayttajat/" +message+ "/kommentit");
                var http = new XMLHttpRequest();
                http.onreadystatechange = function() {
                    if (this.readyState != 4) {
                    console.log(url)
                        return
                    }  
                }

                function addComment() {
                    var post =  document.getElementById("commentField").value;
                    http.open("POST", url,false);
                    http.send(post);
                    $('#commentField').val('');
                    loadComments(); 
                }
               function loadComments(){
                $('#fragmentcomments').load(url);
   
               }
                
                function addReply(messageId) {
                    var replyUrl = encodeURI("/kayttajat/" +message+ "/kommentit/" +messageId+ "/reply");
                    var reply =  document.getElementById("replyField-"+messageId).value;
                    http.open("POST", replyUrl,false);
                    http.send(reply);
                    $('#replyField').val('');
                    loadReply(messageId); 
                    }

               function loadReply(messageId){
                    var replyUrl = encodeURI("/kayttajat/" +message+ "/kommentit" );
                $('#fragmentcomments').load(replyUrl);
   
               }
            function bodyOnLoad(){
                 document.body.onload = function()
                   {
                      loadReply(messageId);
                   }
            }
            

                                    

                                     
