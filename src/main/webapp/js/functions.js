$(document).ready(function(){
    /* --------------------------------------------------------
	Template Settings
    -----------------------------------------------------------*/
    
    var settings =  '<a id="settings" href="#changeSkin" data-toggle="modal">' +
			'<i class="fa fa-gear"></i> 选择颜色' +
		    '</a>' +   
		    '<div class="modal fade" id="changeSkin" tabindex="-1" role="dialog" aria-hidden="true">' +
			'<div class="modal-dialog modal-lg">' +
			    '<div class="modal-content">' +
				'<div class="modal-header">' +
				    '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
				    '<h4 class="modal-title">选  择 背 景 颜 色</h4>' +
				'</div>' +
				'<div class="modal-body">' +
				    '<div class="row template-skins">' +
					'<a data-skin="skin-blur-violate" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-violate.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-lights" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-lights.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-city" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-city.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-greenish" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-greenish.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-night" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-night.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-blue" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-blue.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-sunny" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-sunny.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-cloth" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-cloth.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-tectile" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-tectile.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-chrome" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-chrome.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-ocean" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-ocean.jpg" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-sunset" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/deepblue.png" alt="">' +
					'</a>' +
					'<a data-skin="skin-blur-yellow" class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-yellow.jpg" alt="">' +
					'</a>' +
					'<a  data-skin="skin-blur-kiwi"class="col-sm-2 col-xs-4" href="">' +
					    '<img src="img/skin-kiwi.jpg" alt="">' +
					'</a>' +
				    '</div>' +
				'</div>' +
			    '</div>' +
			'</div>' +
		    '</div>';
    $('#main').prepend(settings);
            
    $('body').on('click', '.template-skins > a', function(e){
	e.preventDefault();
	var skin = $(this).data('skin');
	$('body').attr('id', skin);
	$('#changeSkin').modal('hide');
    });


    /* --------------------------------------------------------
	Components
    -----------------------------------------------------------*/
    (function(){
        /* Textarea */
	if($('.auto-size')[0]) {
	    $('.auto-size').autosize();
	}

        //Select
	if($('.select')[0]) {
	    $('.select').selectpicker();
	}
        
        //Sortable
        if($('.sortable')[0]) {
	    $('.sortable').sortable();
	}
	
        //Tag Select
	if($('.tag-select')[0]) {
	    $('.tag-select').chosen();
	}
        
        /* Tab */
	if($('.tab')[0]) {
	    $('.tab a').click(function(e) {
		e.preventDefault();
		$(this).tab('show');
	    });
	}
        
        /* Collapse */
	if($('.collapse')[0]) {
	    $('.collapse').collapse();
	}
        
        /* Accordion */
        $('.panel-collapse').on('shown.bs.collapse', function () {
            $(this).prev().find('.panel-title a').removeClass('active');
        });

        $('.panel-collapse').on('hidden.bs.collapse', function () {
            $(this).prev().find('.panel-title a').addClass('active');
        });

        //Popover
    	if($('.pover')[0]) {
    	    $('.pover').popover();
    	} 
    })();

    /* --------------------------------------------------------
	Sidebar + Menu
    -----------------------------------------------------------*/
    (function(){
        /* Menu Toggle    click后touchstart */
        $('body').on('click ', '#menu-toggle', function(e){
            e.preventDefault();
            $('html').toggleClass('menu-active');
            $('#sidebar').toggleClass('toggled');
            //$('#content').toggleClass('m-0');
			return false
        });
         
        /* Active Menu */
        $('#sidebar .menu-item').hover(function(){
            $(this).closest('.dropdown').addClass('hovered');
        }, function(){
            $(this).closest('.dropdown').removeClass('hovered');
        });

        /* Prevent */
        $('.side-menu .dropdown > a').click(function(e){
            e.preventDefault();
        });
	

    })();

    /* --------------------------------------------------------
	Chart Info
    -----------------------------------------------------------*/
    (function(){
        $('body').on('click touchstart', '.tile .tile-info-toggle', function(e){
            e.preventDefault();
            $(this).closest('.tile').find('.chart-info').toggle();
        });
    })();

    /* --------------------------------------------------------
	Todo List
    -----------------------------------------------------------*/
    (function(){
        setTimeout(function(){
            //Add line-through for alreadt checked items
            $('.todo-list .media .checked').each(function(){
                $(this).closest('.media').find('.checkbox label').css('text-decoration', 'line-through')
            });

            //Add line-through when checking
            $('.todo-list .media input').on('ifChecked', function(){
                $(this).closest('.media').find('.checkbox label').css('text-decoration', 'line-through');
            });

            $('.todo-list .media input').on('ifUnchecked', function(){
                $(this).closest('.media').find('.checkbox label').removeAttr('style');
            });    
        })
    })();

    /* --------------------------------------------------------
	Custom Scrollbar
    -----------------------------------------------------------*/
    // (function() {
	// if($('.overflow')[0]) {
	//     var overflowRegular, overflowInvisible = false;
	//     overflowRegular = $('.overflow').niceScroll();
	// }
    // })();

    /* --------------------------------------------------------
	Messages + Notifications
    -----------------------------------------------------------*/
    // (function(){
    //     $('body').on('click touchstart', '.drawer-toggle', function(e){
    //         e.preventDefault();
    //         var drawer = $(this).attr('data-drawer');
    //
    //         $('.drawer:not("#'+drawer+'")').removeClass('toggled');
    //
    //         if ($('#'+drawer).hasClass('toggled')) {
    //             $('#'+drawer).removeClass('toggled');
    //         }
    //         else{
    //             $('#'+drawer).addClass('toggled');
    //         }
    //     });
    //
    //     //Close when click outside
    //     $(document).on('mouseup touchstart', function (e) {
    //         var container = $('.drawer, .tm-icon');
    //         if (container.has(e.target).length === 0) {
    //             $('.drawer').removeClass('toggled');
    //             $('.drawer-toggle').removeClass('open');
    //         }
    //     });
    //
    //     //Close
    //     $('body').on('click touchstart', '.drawer-close', function(){
    //         $(this).closest('.drawer').removeClass('toggled');
    //         $('.drawer-toggle').removeClass('open');
    //     });
    // })();


    /* --------------------------------------------------------
	Calendar
    -----------------------------------------------------------*/
    (function(){
	
        //Sidebar
        if ($('#sidebar-calendar')[0]) {
            var date = new Date();
            var d = date.getDate();
            var m = date.getMonth();
            var y = date.getFullYear();
            $('#sidebar-calendar').fullCalendar({
                editable: false,
                events: [],
                header: {
                    left: 'title'
                }
            });
        }

        //Content widget
        if ($('#calendar-widget')[0]) {
            $('#calendar-widget').fullCalendar({
                header: {
                    left: 'title',
                    right: 'prev, next',
                    //right: 'month,basicWeek,basicDay'
                },
                editable: true,
                events: [
                    {
                        title: 'All Day Event',
                        start: new Date(y, m, 1)
                    },
                    {
                        title: 'Long Event',
                        start: new Date(y, m, d-5),
                        end: new Date(y, m, d-2)
                    },
                    {
                        title: 'Repeat Event',
                        start: new Date(y, m, 3),
                        allDay: false
                    },
                    {
                        title: 'Repeat Event',
                        start: new Date(y, m, 4),
                        allDay: false
                    }
                ]
            });
        }

    })();

    /* --------------------------------------------------------
	RSS Feed widget
    -----------------------------------------------------------*/
    // (function(){
	// if($('#news-feed')[0]){
	//     $('#news-feed').FeedEk({
	// 	FeedUrl: 'http://rss.cnn.com/rss/edition.rss',
	// 	MaxCount: 5,
	// 	ShowDesc: false,
	// 	ShowPubDate: true,
	// 	DescCharacterLimit: 0
	//     });
	// }
    // })();

    /* --------------------------------------------------------
	Chat
    -----------------------------------------------------------*/
    // $(function() {
    //     $('body').on('click touchstart', '.chat-list-toggle', function(){
    //         $(this).closest('.chat').find('.chat-list').toggleClass('toggled');
    //     });
    //
    //     $('body').on('click touchstart', '.chat .chat-header .btn', function(e){
    //         e.preventDefault();
    //         $('.chat .chat-list').removeClass('toggled');
    //         $(this).closest('.chat').toggleClass('toggled');
    //     });
    //
    //     $(document).on('mouseup touchstart', function (e) {
    //         var container = $('.chat, .chat .chat-list');
    //         if (container.has(e.target).length === 0) {
    //             container.removeClass('toggled');
    //         }
    //     });
    // });

    /* --------------------------------------------------------
	Form Validation
    -----------------------------------------------------------*/
    (function(){
	if($("[class*='form-validation']")[0]) {
	    $("[class*='form-validation']").validationEngine();

	    //Clear Prompt
	    $('body').on('click', '.validation-clear', function(e){
		e.preventDefault();
		$(this).closest('form').validationEngine('hide');
	    });
	}
    })();

    /* --------------------------------------------------------
     `Color Picker
    -----------------------------------------------------------*/
    (function(){
        //Default - hex
	if($('.color-picker')[0]) {
	    $('.color-picker').colorpicker();
	}
        
        //RGB
	if($('.color-picker-rgb')[0]) {
	    $('.color-picker-rgb').colorpicker({
		format: 'rgb'
	    });
	}
        
        //RGBA
	if($('.color-picker-rgba')[0]) {
	    $('.color-picker-rgba').colorpicker({
		format: 'rgba'
	    });
	}
	
	//Output Color
	if($('[class*="color-picker"]')[0]) {
	    $('[class*="color-picker"]').colorpicker().on('changeColor', function(e){
		var colorThis = $(this).val();
		$(this).closest('.color-pick').find('.color-preview').css('background',e.color.toHex());
	    });
	}
    })();

    /* --------------------------------------------------------
     Date Time Picker
     -----------------------------------------------------------*/
    (function(){
        //Date Only
	if($('.date-only')[0]) {
	    $('.date-only').datetimepicker({
		pickTime: false
	    });
	}

        //Time only
	if($('.time-only')[0]) {
	    $('.time-only').datetimepicker({
		pickDate: false
	    });
	}

        //12 Hour Time
	if($('.time-only-12')[0]) {
	    $('.time-only-12').datetimepicker({
		pickDate: false,
		pick12HourFormat: true
	    });
	}
        
        $('.datetime-pick input:text').on('click', function(){
            $(this).closest('.datetime-pick').find('.add-on i').click();
        });
    })();

    /* --------------------------------------------------------
     Input Slider
     -----------------------------------------------------------*/
    (function(){
	if($('.input-slider')[0]) {
	    $('.input-slider').slider().on('slide', function(ev){
		$(this).closest('.slider-container').find('.slider-value').val(ev.value);
	    });
	}
    })();

    /* --------------------------------------------------------
     WYSIWYE Editor + Markedown
     -----------------------------------------------------------*/
    (function(){
        //Markedown
	if($('.markdown-editor')[0]) {
	    $('.markdown-editor').markdown({
		autofocus:false,
		savable:false
	    });
	}
        
        //WYSIWYE Editor
	if($('.wysiwye-editor')[0]) {
	    $('.wysiwye-editor').summernote({
		height: 200
	    });
	}
        
    })();

    /* --------------------------------------------------------
     Media Player
     -----------------------------------------------------------*/
    // (function(){
	// if($('audio, video')[0]) {
	//     $('audio,video').mediaelementplayer({
	// 	success: function(player, node) {
	// 	    $('#' + node.id + '-mode').html('mode: ' + player.pluginType);
	// 	}
	//     });
	// }
    // })();

    /* ---------------------------
	Image Popup [Pirobox]
    --------------------------- */
    (function() {
	if($('.pirobox_gall')[0]) {
	    //Fix IE
	    jQuery.browser = {};
	    (function () {
		jQuery.browser.msie = false;
		jQuery.browser.version = 0;
		if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
		    jQuery.browser.msie = true;
		    jQuery.browser.version = RegExp.$1;
		}
	    })();
	    
	    //Lightbox
	    $().piroBox_ext({
		piro_speed : 700,
		bg_alpha : 0.5,
		piro_scroll : true // pirobox always positioned at the center of the page
	    });
	}
    })();

    /* ---------------------------
     Vertical tab
     --------------------------- */
    (function(){
        $('.tab-vertical').each(function(){
            var tabHeight = $(this).outerHeight();
            var tabContentHeight = $(this).closest('.tab-container').find('.tab-content').outerHeight();

            if ((tabContentHeight) > (tabHeight)) {
                $(this).height(tabContentHeight);
            }
        })

        $('body').on('click touchstart', '.tab-vertical li', function(){
            var tabVertical = $(this).closest('.tab-vertical');
            tabVertical.height('auto');

            var tabHeight = tabVertical.outerHeight();
            var tabContentHeight = $(this).closest('.tab-container').find('.tab-content').outerHeight();

            if ((tabContentHeight) > (tabHeight)) {
                tabVertical.height(tabContentHeight);
            }
        });


    })();
    
    /* --------------------------------------------------------
     Login + Sign up
    -----------------------------------------------------------*/
    (function(){
	$('body').on('click touchstart', '.box-switcher', function(e){
	    e.preventDefault();
	    var box = $(this).attr('data-switch');
	    $(this).closest('.box').toggleClass('active');
	    $('#'+box).closest('.box').addClass('active'); 
	});
    })();
    
   
    
    /* --------------------------------------------------------
     Checkbox + Radio
     -----------------------------------------------------------*/
    if($('input:checkbox, input:radio')[0]) {
    	
	//Checkbox + Radio skin
	$('input:checkbox:not([data-toggle="buttons"] input, .make-switch input), input:radio:not([data-toggle="buttons"] input)').iCheck({
		    checkboxClass: 'icheckbox_minimal',
		    radioClass: 'iradio_minimal',
		    increaseArea: '20%' // optional
	});
    
	//Checkbox listing
	var parentCheck = $('.list-parent-check');
	var listCheck = $('.list-check');
    
	parentCheck.on('ifChecked', function(){
		$(this).closest('.list-container').find('.list-check').iCheck('check');
	});
    
	parentCheck.on('ifClicked', function(){
		$(this).closest('.list-container').find('.list-check').iCheck('uncheck');
	});
    
	listCheck.on('ifChecked', function(){
		    var parent = $(this).closest('.list-container').find('.list-parent-check');
		    var thisCheck = $(this).closest('.list-container').find('.list-check');
		    var thisChecked = $(this).closest('.list-container').find('.list-check:checked');
	    
		    if(thisCheck.length == thisChecked.length) {
			parent.iCheck('check');
		    }
	});
    
	listCheck.on('ifUnchecked', function(){
		    var parent = $(this).closest('.list-container').find('.list-parent-check');
		    parent.iCheck('uncheck');
	});
    
	listCheck.on('ifChanged', function(){
		    var thisChecked = $(this).closest('.list-container').find('.list-check:checked');
		    var showon = $(this).closest('.list-container').find('.show-on');
		    if(thisChecked.length > 0 ) {
			showon.show();
		    }
		    else {
			showon.hide();
		    }
	});
	   
    }
    
    /* --------------------------------------------------------
        MAC Hack 
    -----------------------------------------------------------*/
    (function(){
	//Mac only
        if(navigator.userAgent.indexOf('Mac') > 0) {
            $('body').addClass('mac-os');
        }
    })();


	/* --------------------------------------------------------
    Date Time Widget
    -----------------------------------------------------------*/
	(function(){
		var monthNames = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ];
		var dayNames= ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]

		// Create a newDate() object
		var newDate = new Date();

		// Extract the current date from Date object
		newDate.setDate(newDate.getDate());

		// Output the day, date, month and year
		$('#date').html(dayNames[newDate.getDay()] + " " + newDate.getDate() + ' ' + monthNames[newDate.getMonth()] + ' ' + newDate.getFullYear());

		setInterval( function() {

			// Create a newDate() object and extract the seconds of the current time on the visitor's
			var seconds = new Date().getSeconds();

			// Add a leading zero to seconds value
			$("#sec").html(( seconds < 10 ? "0":"" ) + seconds);
		},1000);

		setInterval( function() {

			// Create a newDate() object and extract the minutes of the current time on the visitor's
			var minutes = new Date().getMinutes();

			// Add a leading zero to the minutes value
			$("#min").html(( minutes < 10 ? "0":"" ) + minutes);
		},1000);

		setInterval( function() {

			// Create a newDate() object and extract the hours of the current time on the visitor's
			var hours = new Date().getHours();

			// Add a leading zero to the hours value
			$("#hours").html(( hours < 10 ? "0" : "" ) + hours);
		}, 1000);



	})();

	/* --------------------------------------------------------
    Video ZG-CHINA
    -----------------------------------------------------------*/
	// (function(){
	// 		// 设置中文
	// 		videojs.addLanguage('zh-CN', {
	// 			"Play": "播放",
	// 			"Pause": "暂停",
	// 			"Current Time": "当前时间",
	// 			"Duration": "时长",
	// 			"Remaining Time": "剩余时间",
	// 			"Stream Type": "媒体流类型",
	// 			"LIVE": "直播",
	// 			"Loaded": "加载完毕",
	// 			"Progress": "进度",
	// 			"Fullscreen": "全屏",
	// 			"Non-Fullscreen": "退出全屏",
	// 			"Mute": "静音",
	// 			"Unmute": "取消静音",
	// 			"Playback Rate": "播放速度",
	// 			"Subtitles": "字幕",
	// 			"subtitles off": "关闭字幕",
	// 			"Captions": "内嵌字幕",
	// 			"captions off": "关闭内嵌字幕",
	// 			"Chapters": "节目段落",
	// 			"Close Modal Dialog": "关闭弹窗",
	// 			"Descriptions": "描述",
	// 			"descriptions off": "关闭描述",
	// 			"Audio Track": "音轨",
	// 			"You aborted the media playback": "视频播放被终止",
	// 			"A network error caused the media download to fail part-way.": "网络错误导致视频下载中途失败。",
	// 			"The media could not be loaded, either because the server or network failed or because the format is not supported.": "视频因格式不支持或者服务器或网络的问题无法加载。",
	// 			"The media playback was aborted due to a corruption problem or because the media used features your browser did not support.": "由于视频文件损坏或是该视频使用了你的浏览器不支持的功能，播放终止。",
	// 			"No compatible source was found for this media.": "无法找到此视频兼容的源。",
	// 			"The media is encrypted and we do not have the keys to decrypt it.": "视频已加密，无法解密。",
	// 			"Play Video": "播放视频",
	// 			"Close": "关闭",
	// 			"Modal Window": "弹窗",
	// 			"This is a modal window": "这是一个弹窗",
	// 			"This modal can be closed by pressing the Escape key or activating the close button.": "可以按ESC按键或启用关闭按钮来关闭此弹窗。",
	// 			", opens captions settings dialog": ", 开启标题设置弹窗",
	// 			", opens subtitles settings dialog": ", 开启字幕设置弹窗",
	// 			", opens descriptions settings dialog": ", 开启描述设置弹窗",
	// 			", selected": ", 选择",
	// 			"captions settings": "字幕设定",
	// 			"Audio Player": "音频播放器",
	// 			"Video Player": "视频播放器",
	// 			"Replay": "重播",
	// 			"Progress Bar": "进度小节",
	// 			"Volume Level": "音量",
	// 			"subtitles settings": "字幕设定",
	// 			"descriptions settings": "描述设定",
	// 			"Text": "文字",
	// 			"White": "白",
	// 			"Black": "黑",
	// 			"Red": "红",
	// 			"Green": "绿",
	// 			"Blue": "蓝",
	// 			"Yellow": "黄",
	// 			"Magenta": "紫红",
	// 			"Cyan": "青",
	// 			"Background": "背景",
	// 			"Window": "视窗",
	// 			"Transparent": "透明",
	// 			"Semi-Transparent": "半透明",
	// 			"Opaque": "不透明",
	// 			"Font Size": "字体尺寸",
	// 			"Text Edge Style": "字体边缘样式",
	// 			"None": "无",
	// 			"Raised": "浮雕",
	// 			"Depressed": "压低",
	// 			"Uniform": "均匀",
	// 			"Dropshadow": "下阴影",
	// 			"Font Family": "字体库",
	// 			"Proportional Sans-Serif": "比例无细体",
	// 			"Monospace Sans-Serif": "单间隔无细体",
	// 			"Proportional Serif": "比例细体",
	// 			"Monospace Serif": "单间隔细体",
	// 			"Casual": "舒适",
	// 			"Script": "手写体",
	// 			"Small Caps": "小型大写字体",
	// 			"Reset": "重启",
	// 			"restore all settings to the default values": "恢复全部设定至预设值",
	// 			"Done": "完成",
	// 			"Caption Settings Dialog": "字幕设定视窗",
	// 			"Beginning of dialog window. Escape will cancel and close the window.": "开始对话视窗。离开会取消及关闭视窗",
	// 			"End of dialog window.": "结束对话视窗"
	// 		});
	// 	})();

	/* --------------------------------------------------------
	REWRITE ALERT
	 tabindex="-1" 使用esc关闭
	-----------------------------------------------------------*/
	(function(){
		window.Ewin = function () {
			var html = '<div id="[Id]" class="modal fade" tabindex="-1"  role="dialog" aria-labelledby="modalLabel">' +
				'<div class="modal-dialog modal-sm">' +
				'<div class="modal-content">' +
				'<div class="modal-header">' +
				'<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
				'<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
				'</div>' +
				'<div class="modal-body">' +
				'<p>[Message]</p>' +
				'</div>' +
				'<div class="modal-footer">' +
				'<button type="button" class="btn btn-default cancel" data-dismiss="modal">[BtnCancel]</button>' +
				'<button type="button" class="btn btn-primary ok" data-dismiss="modal">[BtnOk]</button>' +
				'</div>' +
				'</div>' +
				'</div>' +
				'</div>';


			var dialogdHtml = '<div id="[Id]" class="modal fade" tabindex="-1"  role="dialog" aria-labelledby="modalLabel">' +
				'<div class="modal-dialog">' +
				'<div class="modal-content">' +
				'<div class="modal-header">' +
				'<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
				'<h4 class="modal-title" id="modalLabel">[Title]</h4>' +
				'</div>' +
				'<div class="modal-body">' +
				'</div>' +
				'</div>' +
				'</div>' +
				'</div>';
			var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
			var generateId = function () {
				var date = new Date();
				return 'mdl' + date.valueOf();
			};
			$("body").removeClass("modal-open");		//移处overflow hidden

			var init = function (options) {
				options = $.extend({}, {
					title: "操作提示",
					message: "提示内容",
					btnok: "确定",
					btncl: "取消",
					width: 200,
					auto: false
				}, options || {});
				var modalId = generateId();
				var content = html.replace(reg, function (node, key) {
					return {
						Id: modalId,
						Title: options.title,
						Message: options.message,
						BtnOk: options.btnok,
						BtnCancel: options.btncl
					}[key];
				});
				$('body').append(content);
				$('#' + modalId).modal({
					width: options.width,
					backdrop: 'static'
				});
				$('#' + modalId).on('hide.bs.modal', function (e) {
					$('body').find('#' + modalId).remove();
				});
				return modalId;
			};

			return {
				alert: function (options) {
					if (typeof options == 'string') {
						options = {
							message: options
						};
					}
					var id = init(options);
					var modal = $('#' + id);
					modal.find('.ok').removeClass('btn-success').addClass('btn-primary');
					modal.find('.cancel').hide();
					$("body").removeClass("modal-open");		//移处overflow hidden

					return {
						id: id,
						on: function (callback) {
							if (callback && callback instanceof Function) {
								modal.find('.ok').click(function () { callback(true); });
							}
						},
						hide: function (callback) {
							if (callback && callback instanceof Function) {
								modal.on('hide.bs.modal', function (e) {
									callback(e);
								});
							}
						}
					};
				},
				confirm: function (options) {
					var id = init(options);
					var modal = $('#' + id);
					modal.find('.ok').removeClass('btn-primary').addClass('btn-success');
					modal.find('.cancel').show();
					return {
						id: id,
						on: function (callback) {
							if (callback && callback instanceof Function) {
								modal.find('.ok').click(function () { callback(true); });
								modal.find('.cancel').click(function () { callback(false); });
							}
						},
						hide: function (callback) {
							if (callback && callback instanceof Function) {
								modal.on('hide.bs.modal', function (e) {
									callback(e);
								});
							}
						}
					};
				},
				dialog: function (options) {
					options = $.extend({}, {
						title: 'title',
						url: '',
						width: 800,
						height: 550,
						onReady: function () { },
						onShown: function (e) { }
					}, options || {});
					var modalId = generateId();

					var content = dialogdHtml.replace(reg, function (node, key) {
						return {
							Id: modalId,
							Title: options.title
						}[key];
					});
					$('body').append(content);
					var target = $('#' + modalId);
					target.find('.modal-body').load(options.url);
					if (options.onReady())
						options.onReady.call(target);
					target.modal();
					target.on('shown.bs.modal', function (e) {
						if (options.onReady(e))
							options.onReady.call(target, e);
					});
					target.on('hide.bs.modal', function (e) {
						$('body').find(target).remove();
					});

				}
			}
		}();
	})();


});

$(window).load(function(){
    /* --------------------------------------------------------
     Tooltips
     -----------------------------------------------------------*/
    (function(){
        if($('.tooltips')[0]) {
            $('.tooltips').tooltip();
        }
    })();

    /* --------------------------------------------------------
     Animate numbers
     -----------------------------------------------------------*/
    $('.quick-stats').each(function(){
        var target = $(this).find('h2');
        var toAnimate = $(this).find('h2').attr('data-value');
        // Animate the element's value from x to y:
        $({someValue: 0}).animate({someValue: toAnimate}, {
            duration: 1000,
            easing:'swing', // can be anything
            step: function() { // called on every step
                // Update the element's text with rounded-up value:
                target.text(commaSeparateNumber(Math.round(this.someValue)));
            }
        });

        function commaSeparateNumber(val){
            while (/(\d+)(\d{3})/.test(val.toString())){
                val = val.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
            }
            return val;
        }
    });

	/**
	 * 退出登录
	 * @关键字
	 */
	(function(){

		if($.cookie("adminName")){
			$("#username").html($.cookie("adminName"));
		}

		$("#loginOut").on("click" , function(){
			Ewin.confirm({ message: "确定退出？",btnok:"退出" }).on(function (e) {
				if (!e) {
					return;
				}
				$.removeCookie("adminName");
				$.removeCookie("adminId");
				window.location.href = "login.html";
			});
		})
	})();

	/**
	 * 返回顶部
	 * @关键字
	 */
	(function(){
		var fn = function(){
			var scrollH = $("body").scrollTop();
			var htscrollH = $("html").scrollTop()
			var winH = $(window).height();
			var docH = $(document).height();
			if(scrollH > 0 || htscrollH>0){
				$("#goTop").removeClass("hidden");
			}else{
				$("#goTop").addClass("hidden");

			}
		};

		if($(window).width() > 768){
			fn();
			$("body").scroll( function(){
				fn();
			});
			$("#goTop").on("click" , function(){
				$("body").stop().animate({scrollTop:"0"});
				return false;
			});
		}else{
			$("#goTop").removeClass("hidden")
		}




	})();

});



// //




