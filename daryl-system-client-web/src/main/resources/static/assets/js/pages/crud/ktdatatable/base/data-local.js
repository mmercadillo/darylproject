'use strict';
// Class definition

var KTDatatableDataLocalDemo = function() {
    // Private functions

    // demo initializer
    var demo = function() {
       
        var datatable = $('#kt_datatable').KTDatatable({
            // datasource definition
            data: {
                type: 'local',
                source: dataJSONArray,
                pageSize: 5,
            },

            // layout definition
            layout: {
                scroll: true, // enable/disable datatable scroll both horizontal and vertical when needed.
                // height: 450, // datatable's body's fixed height
                footer: false, // display/hide footer
            },

            // column sorting
            sortable: true,

            pagination: true,
			/*
            search: {
                input: $('#kt_datatable_search_query'),
                key: 'generalSearch'
            },*/
			

            // columns definition
            columns: [/*{
                field: 'RecordID',
                title: '#',
                sortable: false,
                width: 20,
                type: 'number',
                selector: {
                    class: ''
                },
                textAlign: 'center',
            }, */{
                field: 'robot',
                title: 'ROBOT',
				sortable: true,
				autoHide: false,
				textAlign: 'left',
				overflow: 'visible',
				template: function(row) {
					return '<a href = "chart/'+row.robot+'" >'+row.robot+'</a>';
				},
            },/* {
                field: 'fAlta',
                title: 'F. Inicio'
            },*/ {
                field: 'fModificacion',
                title: 'F. Últ. Act.',
				autoHide: false,
				textAlign: 'left',
				overflow: 'visible',
            }, {
                field: 'total',
                title: 'Total',
				sortable: true,
				autoHide: false,
				textAlign: 'left',
				overflow: 'visible',
				template: function(row){
					if(row.total > 0){
						return '<span class="font-weight-bold text-success">' +
                        row.total + '</span>';
					}else{
						return '<span class="font-weight-bold text-danger">' +
                        row.total + '</span>';
					}
				},
            }, {
                field: 'numOperaciones',
                title: 'Ops.',
				sortable: false,
				autoHide: false,
				textAlign: 'left',
				overflow: 'visible',
            },{
                field: 'numOpsGanadoras',
                title: 'Ops. W.',
				sortable: false,
				autoHide: false,
				textAlign: 'left',
				overflow: 'visible',
            },{
                field: 'numOpsPerdedoras',
                title: 'Ops. L.',
				sortable: false,
				autoHide: false,
				textAlign: 'left',
				overflow: 'visible',
            },{
                field: 'pctOpsGanadoras',
                title: '%Ops W.',
				sortable: false,
				autoHide: false,
				textAlign: 'left',
				overflow: 'visible',
				template: function(row){
					return row.pctOpsGanadoras.toFixed(2) + '%';
				},
            },{
                field: 'pctOpsPerdedoras',
                title: '%Ops L.',
				sortable: false,
				autoHide: false,
				textAlign: 'left',
				overflow: 'visible',
				template: function(row){
					return row.pctOpsPerdedoras.toFixed(2) + '%';
				},
            },/*{
                field: 'status',
                title: 'Riesgo',
				autoHide: false,
				textAlign: 'center',
                // callback function support for column rendering
                template: function(row) {
                    var status = {
                        1: {
                            'title': 'Pending',
                            'class': ' label-light-success'
                        },
                        2: {
                            'title': 'Delivered',
                            'class': ' label-light-danger'
                        },
                        3: {
                            'title': 'Canceled',
                            'class': ' label-light-primary'
                        },
                        4: {
                            'title': 'Success',
                            'class': ' label-light-success'
                        },
                        5: {
                            'title': 'Info',
                            'class': ' label-light-info'
                        },
                        6: {
                            'title': 'Muy alto',
                            'class': ' label-light-danger'
                        },
                        7: {
                            'title': 'Warning',
                            'class': ' label-light-warning'
                        },
                    };
                    return '<span class="label font-weight-bold label-lg ' + status[row.status].class + ' label-inline">' + status[row.status].title + '</span>';
                },
            },{
                field: 'type',
                title: 'Estado',
                autoHide: false,
				textAlign: 'left',
                // callback function support for column rendering
                template: function(row) {
                    var status = {
                        1: {
                            'title': 'On',
                            'state': 'success'
                        },
                        2: {
                            'title': 'Off',
                            'state': 'danger'
                        },
                    };
                    return '<span class="font-weight-bold text-' + status[row.type].state + '">' +
                        status[row.type].title + '</span>';
                },
            }, {
                field: 'actions',
                title: 'Actions',
                sortable: false,
                width: 125,
                overflow: 'visible',
                autoHide: false,
                template: function() {
                    return '\
							<div class="dropdown dropdown-inline">\
								<a href="javascript:;" class="btn btn-sm btn-clean btn-icon mr-2" data-toggle="dropdown">\
	                                <span class="svg-icon svg-icon-md">\
	                                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1">\
	                                        <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">\
	                                            <rect x="0" y="0" width="24" height="24"/>\
	                                            <path d="M5,8.6862915 L5,5 L8.6862915,5 L11.5857864,2.10050506 L14.4852814,5 L19,5 L19,9.51471863 L21.4852814,12 L19,14.4852814 L19,19 L14.4852814,19 L11.5857864,21.8994949 L8.6862915,19 L5,19 L5,15.3137085 L1.6862915,12 L5,8.6862915 Z M12,15 C13.6568542,15 15,13.6568542 15,12 C15,10.3431458 13.6568542,9 12,9 C10.3431458,9 9,10.3431458 9,12 C9,13.6568542 10.3431458,15 12,15 Z" fill="#000000"/>\
	                                        </g>\
	                                    </svg>\
	                                </span>\
	                            </a>\
							  	<div class="dropdown-menu dropdown-menu-sm dropdown-menu-right">\
	                                <ul class="navi flex-column navi-hover py-2">\
	                                    <li class="navi-header font-weight-bolder text-uppercase font-size-xs text-primary pb-2">\
	                                        Elije una acción:\
	                                    </li>\
	                                    <li class="navi-item">\
	                                        <a href="#" class="navi-link">\
	                                            <span class="navi-icon"><i class="la la-info"></i></span>\
	                                            <span class="navi-text">Información</span>\
	                                        </a>\
	                                    </li>\
	                                    <li class="navi-item">\
	                                        <a href="#" class="navi-link">\
	                                            <span class="navi-icon"><i class="la la-download"></i></span>\
	                                            <span class="navi-text">Descarga</span>\
	                                        </a>\
	                                    </li>\
	                                </ul>\
							  	</div>\
							</div>\
						';
                },
            }*/],
        });
		
        $('#kt_datatable_search_status').on('change', function() {
            datatable.search($(this).val().toLowerCase(), 'Riesgo');
        });

        $('#kt_datatable_search_type').on('change', function() {
            datatable.search($(this).val().toLowerCase(), 'Estado');
        });

        $('#kt_datatable_search_status, #kt_datatable_search_type').selectpicker();
		
    };

    return {
        // Public functions
        init: function() {
            // init dmeo
            demo();
        },
    };
}();

jQuery(document).ready(function() {
    KTDatatableDataLocalDemo.init();
});
