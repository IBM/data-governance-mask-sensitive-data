/**
  *
  * main() will be run when you invoke this action
  *
  * @param Cloud Functions actions accept a single parameter, which must be a JSON object.
  *
  * @return The output of this action, which must be a JSON object.
  *
  */
 async function main(params) {
     
 const axios = require('axios');

 var response = await axios.post('http://<db-rest-app-base-url>/orders', {
        "emailid": params.emailid,
        "action": params.action
      })
      .then(res => {
        return res.data
      })
      .catch(error => {
        return {"result": error}
      })
      
       
return response;     
}

