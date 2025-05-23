console.log("contacts page script")

const viewContactModal = document.getElementById('view_contact_modal');

// set the modal menu element
const $targetEl = document.getElementById('modalEl');

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};


const contactModal = new Modal(viewContactModal, options, instanceOptions);

function openContactModal() {
    contactModal.show();
} 

function closeContactModal() {
    contactModal.hide();
}   


async function loadContactData(id){
    console.log(id)
try {
    const data = await(await fetch(`http://localhost:8080/api/contact/${id}`)).json();
    console.log(data);
    document.querySelector('#contact_name').innerHTML = data.name;
    document.querySelector('#contact_email').innerHTML = data.email;
    document.querySelector('#contact_phone').innerHTML = data.phoneNumber;
    document.querySelector('#contact_address').innerHTML = data.address;
    document.querySelector('#contact_picture').src = data.picture;
    document.querySelector('#contact_description').innerHTML = data.description;

    // // Set links
    document.querySelector("#contact_website").href = data.website || "#";
    document.querySelector("#contact_website").innerText = data.website ? "Website" : "No Website";

    document.querySelector("#contact_linkedin").href = data.linkedin || "#";
    document.querySelector("#contact_linkedin").innerText = data.linkedin ? "LinkedIn" : "No LinkedIn";

    // Set favorite status with an icon
    document.querySelector("#contact_favorite").innerHTML = data.favorite
    ? '<i class="fa-solid fa-star text-yellow-500"></i>' : '<i> </i>';
    // : '<i class="fa-regular fa-star text-gray-400"></i>';
    
    openContactModal();
} catch (error) {
    console.log(error)
}
 
}



// delete modal


console.log("delete page script")

const deleteContactModal = document.getElementById('delete_contact_modal');

const deleteModal = new Modal(deleteContactModal, options, instanceOptions);

let contactId = null;

function openDeleteModal(id ,  contactName) {
    contactId = id;

    document.getElementById('confirm_delete_message').innerHTML = `Are you sure you want to delete <span style="color: red; font-weight: bold;">${contactName}</span>?`;
    deleteModal.show();
}

function closeDeleteModal() {
    deleteModal.hide();
}



async function deleteContact() {
    try {
        const response = await fetch(`http://localhost:8080/api/contact/delete/${contactId}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        });
        console.log(contactId)
        if (response.ok) {
            // alert("Contact deleted successfully");
            closeDeleteModal();
            window.location.reload();
        } else {
            alert("Failed to delete contact");
        }
    } catch (error) {
        console.log(error);
        alert("An error occurred while deleting the contact.");
    }

}