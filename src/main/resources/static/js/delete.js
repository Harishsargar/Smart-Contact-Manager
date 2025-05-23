console.log("delete page script")

const deleteContactModal = document.getElementById('delete_contact_modal');

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
    id: 'modalEl',
    override: true
  };


const deleteModal = new Modal(deleteContactModal, options, instanceOptions);

function openDeleteModal() {
    deleteModal.show();
}

function closeDeleteModal() {
    deleteModal.hide();
}
