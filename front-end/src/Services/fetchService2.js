// function grabAndAuthorizeRequestFromTheServerFiles(url, requestMethod, jwt, requestBody) {
//     const fetchData = {
//         headers: {
//             "Content-Type": "multipart/formData"
//         },
//         method: requestMethod
//     };
//
//     if(jwt) {
//         fetchData.headers.Authorization = `Bearer ${jwt}`;
//     }
//
//     if(requestBody)  {
//         fetchData.body = JSON.stringify(requestBody);
//     }
//
//     return fetch(url, fetchData).then((response) => {
//         if (response.status === 200) {
//             const contentType = response.headers.get("content-type");
//             if (contentType && contentType.indexOf("multipart/formData") !== -1) {
//                 return response.json();
//             } else {
//                 return response.text();
//             }
//         }
//     });
// }
//
// export default grabAndAuthorizeRequestFromTheServerFiles;