$( document ).ready(function() {
  $('#tenantCheck').click(function(event) {
    if ($('#tenantCheck').attr('checked')) {
    	$('#tenantIdRow').show();
    	$('#tenantNameRow').hide();
    } else {
    	$('#tenantIdRow').hide();
    	$('#tenantNameRow').show();
    }
  });
});