$(document).ready(function() {
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();

	$('#calendar').fullCalendar({
		height: 850,
		minTime: 7,
		axisFormat: 'HH:mm',
		timeFormat: 'HH:mm',
		allDaySlot: false,
		slotEventOverlap: false,
		header:
		{
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		defaultView: 'agendaWeek',
		%%%EVENTS%%%
	})
});