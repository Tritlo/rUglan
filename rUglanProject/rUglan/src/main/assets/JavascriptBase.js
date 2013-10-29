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
			left: 'month,agendaWeek,agendaDay',
			center: 'title',
			right: 'prev,today,next'
		},
		titleFormat: {
		    month: 'MMMM yyyy',
		    week: "d. MMM[ yyyy]{ '&#8212;'[ d. MMM] yyyy}",
		    day: 'dddd<br>d. MMM yyyy'
		},
		monthNames: ['Janúar', 'Febrúar', 'Mars', 'Apríl', 'Maí', 'Júní', 'Júlí', 'Ágúst', 'September', 'Október', 'Nóvember', 'Desember'],
		monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'Maí', 'Jún', 'Júl', 'Ágú', 'Sep', 'Okt', 'Nóv', 'Des'],
		dayNames: ['Sunnudagur', 'Mánudagur', 'Þriðjudagur', 'Miðvikudagur', 'Fimmtudagur', 'Föstudagur', 'Laugardagur'],
		dayNamesShort: ['Sun', 'Mán', 'Þri', 'Mið', 'Fim', 'Fös', 'Lau'],
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
		%%%EVENTS%%%
	})
	$(".fc-header").css("width", "auto");
	$(".fc-header").css("margin", "auto");
});