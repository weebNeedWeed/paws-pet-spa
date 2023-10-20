window.addEventListener("DOMContentLoaded", (_) =>  {
   const servicesTable = document.getElementById("servicesTable");

   new simpleDatatables.DataTable(servicesTable, {
       searchable: false,
       paging: false,
       sortable: false
   });
});