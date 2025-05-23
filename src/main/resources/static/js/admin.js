console.log("admin js loaded");

document.querySelector("#image_file_input").addEventListener("change", function(event) {
    let file = event.target.files[0];
    if (file) {
        let reader = new FileReader();
        reader.onload = function(e) {      
            let preview = document.querySelector("#upload_image_preview"); // Define preview
            preview.src = e.target.result;
            preview.classList.remove("hidden"); // Ensure the image is visible
            preview.classList.add("block"); // Apply Tailwind block class
        };
        reader.readAsDataURL(file);
    }
});



// document.addEventListener("DOMContentLoaded", function () {
//     const existingProfilePicture = document.getElementById("existingProfilePicture").value;
//     const previewImage = document.getElementById("upload_image_preview");

//     // Set the initial preview image if it exists
//     if (existingProfilePicture) {
//         previewImage.src = existingProfilePicture;
//     }

//     // Function to update preview when a new image is selected
//     window.previewImage = function (event) {
//         const file = event.target.files[0];
//         if (file) {
//             const reader = new FileReader();
//             reader.onload = function (e) {
//                 previewImage.src = e.target.result;
//             };
//             reader.readAsDataURL(file);
//         }
//     };
// });
