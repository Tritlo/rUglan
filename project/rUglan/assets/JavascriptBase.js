$(document).ready(function() {
	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();

	$('#calendar').fullCalendar({
		height: 850,
		minTime: 7,
		axisFormat: 'HH:mm',
		timeFormat: 'HH:mm { - HH:mm}',
		allDaySlot: false,
		weekends: false,
		slotEventOverlap: false,
		firstDay: 1,
		header:
		{
			left: 'month,agendaWeek,agendaDay',
			center: 'title',
			right: 'prev,today,next'
		},
		titleFormat: {
		    month: 'MMMM yyyy',
		    week: "d. MMM[ yyyy]{ '&#8212;'[ d. MMM] yyyy}",
		    day: 'dddd<br>d. MMM yyyy'
		},
		monthNames: ['janúar', 'febrúar', 'mars', 'apríl', 'maí', 'júní', 'júlí', 'ágúst', 'september', 'október', 'nóvember', 'desember'],
		monthNamesShort: ['jan', 'feb', 'mar', 'apr', 'maí', 'jún', 'júl', 'ágú', 'sep', 'okt', 'nóv', 'des'],
		dayNames: ['sunnudagur', 'mánudagur', 'þriðjudagur', 'miðvikudagur', 'fimmtudagur', 'föstudagur', 'laugardagur'],
		dayNamesShort: ['sun', 'mán', 'þri', 'mið', 'fim', 'fös', 'lau'],
		buttonText: {
		    prev:     '&lsaquo;',
		    next:     '&rsaquo;',
		    prevYear: '&laquo;',
		    nextYear: '&raquo;',
		    today:    'Í dag',
		    month:    'Mánuður',
		    week:     'Vika',
		    day:      'Dagur'
		},
		columnFormat: {
		    month: 'ddd',
		    week: 'd. ddd',
		    day: 'dddd d. MMM'
		},
		defaultView: 'agendaWeek',
		eventTextColor: 'black',
		%%%EVENTS%%%
	})
	$(".fc-header").css("width", "auto");
	$(".fc-header").css("margin", "auto");
	$('#last-updated-label').show();
});