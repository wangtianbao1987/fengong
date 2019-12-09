/**
 * 弹出框
 */
var _alertIndex_ = 0;
var _alertData_ = [];
var closeMyAlert = function(){
	var data = _alertData_[_alertIndex_-1];
	if(data && data.beforeCloseFn){
		if(data.beforeCloseFn(data) == false){
			return;
		}
	}
	$('._alertIndex_'+_alertIndex_).remove();
	_alertIndex_--;
	if(_alertIndex_>0){
		$('._alertIndex_'+_alertIndex_).show();
	}
	if(data && data.afterCloseFn){
		data.afterCloseFn();
	}
};

var myAlert = function(data,conf){
	if(!conf){
		conf = {};
	}
	_alertData_[_alertIndex_] = data;
	
	$('._alertIndex_'+_alertIndex_).hide();
	var div = $('<div>');
	_alertIndex_++;
	
	div.addClass('modal-alert _alertIndex_'+_alertIndex_);
	
	var modelDiv = $('<div>');
	modelDiv.addClass('modal myScrollbar');
	var dialogDiv = $('<div>');
	dialogDiv.addClass('modal-dialog');
    if(conf.dialogCss){
        dialogDiv.css(conf.dialogCss);
    }
	modelDiv.append(dialogDiv);
	var contentDiv = $('<div>');
	contentDiv.addClass('modal-content');
	dialogDiv.append(contentDiv);
	var headerDiv = $('<div>');
	headerDiv.addClass('modal-header');
	contentDiv.append(headerDiv);
	if(!data.hideClose){
		var closeBtn = $('<label>');
		closeBtn.addClass('modal-close-btn');
		closeBtn.html('×');
		closeBtn.on('click',function(){
			if(data.beforeCloseFn){
				if(data.beforeCloseFn.call(closeBtn,div,data) == false){
					return;
				}
			}
			closeMyAlert();
		});
		headerDiv.append(closeBtn);
		if(data.title){
			var title = $('<h4>');
			title.addClass('modal-title');
			title.html(data.title);
			headerDiv.append(title);
		}else{
			headerDiv.append('<span>&nbsp;</span>');
		}
	}else{
		if(data.title){
			var title = $('<h4>');
			title.addClass('modal-title');
			title.html(data.title);
			headerDiv.append(title);
		}
	}
	
	
	var msgDiv = $('<div>');
	msgDiv.addClass('modal-body');
	msgDiv.html(data.message);
	contentDiv.append(msgDiv);
	var footerDiv = $('<div>');
	footerDiv.addClass('modal-footer');
	contentDiv.append(footerDiv);
	if(data.btns){
		$.each(data.btns,function(i,btn){
			var button = $('<label>');
			if(btn.classes){
				button.addClass('modal-btn '+btn.classes);
			}else{
				button.addClass('modal-btn');
			}
			if(btn.attrs){
				$.each(btn.attrs,function(ii,attr){
					button.attr(attr.name,attr.value);
				});
			}
			if(btn.click){
				button.bind('click',function () {
					btn.click.call(button,div,data);
                });
			}
			if(btn.focus){
				button.addClass('model-default-focus');
			}
			button.html(btn.txt);
			footerDiv.append(button);
		});
	}
	if(!data.hideBtn && !data.btns){
		var button = $('<label>');
		button.addClass('modal-btn');
		button.on('click',function(){
			if(data.beforeCloseFn){
				if(data.beforeCloseFn.call(button,div,data) == false){
					return;
				}
			}
			closeMyAlert();
		});
		button.html('确定');
		footerDiv.append(button);
	}
	
	var backdropDiv = $('<div>');
	backdropDiv.addClass('modal-backdrop');
	
	div.append(modelDiv);
	div.append(backdropDiv);
	
	$(document.body).append(div);
	
	if(data.afterLoad){
		data.afterLoad.call(div,data);
	}
	return div;
};

var myConfirm = function(data){
	myAlert({
		message:data.message,
		title:data.title,
		btns:[{
			txt:data.btnOktext||'确定',
			click:function(){
				if(data.btnOkClick){
					var ck = true;
					ck = data.btnOkClick.call(this);
					if(ck){
						closeMyAlert();
					}
				}else{
					closeMyAlert();
				}
			}
		},{
			txt:data.btnCanceltext || '取消',
			click:function(){
				if(data.btnCancelClick){
					var ck = true;
					ck = data.btnCancelClick.call(this);
					if(ck){
						closeMyAlert();
					}
				}else{
					closeMyAlert();
				}
			}
		}],
		beforeCloseFn:function(){
			if(data.btnCloseClick){
				data.btnCloseClick.call(this);
			}
		}
	})
}
