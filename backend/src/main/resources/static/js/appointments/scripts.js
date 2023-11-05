window.addEventListener("DOMContentLoaded", (_) =>  {
    const servicesTable = document.getElementById("appointmentsTable");

    new simpleDatatables.DataTable(servicesTable, {
        searchable: false,
        paging: false,
        sortable: false
    });
});