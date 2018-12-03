$(document).ready(function () {
    $('#sessionsTable').DataTable({
        "scrollY": "65vh",
        "scrollCollapse": true,
        "paging": false
    });
    $('.dataTables_length').addClass('bs-select');
});