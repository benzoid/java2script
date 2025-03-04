/**
 * 
 * @author Bob Hanson 2019.07.06
 * 
 * 
 * A relatively simple ComboBox that supports actual objects, not just strings
 * 
 */
// BH 2019.08.26 text area set to <button> to manage vertical centering. 

// NOTE: If you change this file, then you need to touch and save JQueryUI.java, as only then
//       will the transpiler copy this file to site/swingjs/j2s/swingjs/jquery/

$( function() {
    $('head').append('<style>.j2scb-sel {background-color:#B8CFE5;}'
    		+'\n.j2scb-unsel {background-color:white;}'
    		+'\n.j2scb-hov {background-color:lightblue;}'
    		+'\n.j2scbcont {position:absolute; left:0px;top:0px;}'
    		+'\n.j2scbhead {position:absolute; left:0px;top:0px;text-align:left;overflow:hidden;padding:0px 2px 1px 2px}'
    		+'\n.j2scbbtn {position:absolute; leftbackground-color:white;:100px;top:0px; width:20px;text-align:center;cursor:pointer;background-color:lightblue;padding:0px}'
    		+'\n.j2scbpopup {position:absolute; list-style:none}'
    		+'\n.j2scblist {background-color:white;position:absolute; left:0px;top:0px;margin:0;border:black solid 1px;cursor:pointer;text-align:left;padding:0em;scrollbar-width:thin;cursor:pointer;}</style>'
    );
    
    var CLOSE_DELAY = 100; // BH 2019.10.04 50 was just a bit too fast; could close early
        
    // the widget definition, where 'custom' is the namespace,
    // 'j2sCB' the widget name

    $.widget( 'custom.j2sCB', {
    	
      options: {
    	mode: 's', // or 'm'
 		height: 0,
 		items: null,
 		disabled: false,
 		popupVisible: false,
 		selectedIndex: -1,
 		backgroundColor: "white",
 		// z-index
 		zIndex:999999,
        // Callbacks
        change: null
      },
      itemCount: 0,
      
      id: function() {return this.element[0].id},
      find: function(x) {return this.element.find(x)},
      on: function(a, x) {for(var i = a.length; --i >= 0;)this._on(a[i],x)},
      on2: function(obj, evts, handle) {var a = {};for(var i = evts.length; --i >= 0;)a[evts[i]]=handle;this._on(obj, a)},

  	  popupVisible: function() { return this.options.popupVisible; }, 

  	  setHeight: function(h) {
		  this.options.height = h;
	  },
      setZIndex: function(z) {
    	this.options.zIndex = z;
      },
      
      _mouse: function(e) { 
    	  var opt = $(e.target).closest('.j2scbopt');
    	  this._trigger('change', e, [this, 'mouse', (opt[0] ? opt[0].j2scbIndex : -1)])
      },
      _keyEvent: function(e) {
    	  this._trigger('change', e, [this, 'keyevent']);
      },
 
      // Called when created, and later when changing options
      _refresh: function() {
 
        // Trigger a callback/event
        this._trigger( 'change' , null, [this, "refreshed"] );
      },
 
      // The constructor
      _create: function() {
    	var id = this.id();
        this.element.addClass( 'custom-j2sCB' );
        this.cont = $( '<div>', {'class': 'j2scbcont', 'id':id+'_cont' });
        this.cont.append(this.head = $( '<button>', {'class': 'j2scbhead', 'id':id+'_head' }));
        this.cont.append(this.btn = $( '<button>', {'class': 'j2scbbtn', 'id':id+'_btn' , text:'\u25bc'}));
        this.btn.addClass("swingjs-ui");
        this.popup = $( '<div>', {'class': 'j2scbpopup', 'id':id+'_popup' });
        this.popup.css({
        	display:'none',
        });
        this.list = $( '<ul>', {'class': 'j2scblist', 'id':id+'_list' });
        this.on2(this.list, 'click mousedown touchstart mousemove touchmove mouseup touchend mousewheel mouseover mouseout mouseenter mouseexit'.split(' '), '_mouse');
        this.popup.append(this.list);        
        this.element.append(this.cont);
        // important to add popup after body so that it does not take on any body attributes 
        $('body').after(this.popup);
        this.updateCSS();    	
        this.on( [this.head, this.btn, this.cont], { click: '_open' });
        this.on( [this.popup, this.list], {mouseover: '_stopT' });
        this.on( [this.cont, this.head, this.btn, this.popup, this.list], {
        	mouseleave: '_close'//,
        	//keydown: '_keyEvent'
        		});
        
        if (this.options.items)
        	this.add(this.options.items);
        
        this.setSelectedIndex(this.options.selectedIndex)
        this._refresh();
      },
      // Events bound via _on are removed automatically
      // revert other modifications here
      _destroy: function() {
        // remove generated elements
        this.cont.remove();
 		this.popup.remove();
        this.element
          .removeClass( 'custom-j2sCB' )
          .enableSelection()
          .css( 'background-color', 'transparent' );
		this._trigger('change', null, [this, 'destroyed']);
      },
 
      // _setOptions is called with a hash of all options that are changing
      // always refresh when changing options
      _setOptions: function() {
        // _super and _superApply handle keeping the right this-context
        this._superApply( arguments );
        this._refresh();
      },
 
      // _setOption is called for each individual option that is changing
      _setOption: function( key, value ) {
    	  if (key == "disabled") {
    		  this.options.disabled = true;
    	  }
        //[prevent invalid value here with test and return]
        this._super( key, value );
      },

      update: function(andTrigger) {
   		 var sel = this._selectedItem();
   		 var all;
   		 this.options.selectedIndex = (sel[0] ? sel[0].j2scbIndex : -1);
   		 this.head.text(sel.length ==0 ? '' : 
   			this.options.mode == 's' ? sel.text() : sel.length + ' of ' 
   					+ (all = this.list.find('.j2scbopt').length) + ' selected option' + (all > 1 ? 's' :''));
   		 if (andTrigger)
 	      	this._trigger( 'change' , null, [this, "selected", sel[0].j2scbIndex]);
// 	     else
// 	    	 this._stopT("update");
       },  
      updateList: function(items) {
    	  this.list.children().detach();
    	  this.add(items);
	  },
      add: function(items) {
      	  var y = 0;
    	  if (Array.isArray(items)) {
        	this.itemCount = 0;    		
    	  } else {
    	  	this.list.children().each(function(a) {y += a.height()});
    		items = [items];  
    	  }
    	  for (var i = 0; i < items.length; i++) {
    		var item = items[i];
    		if (!item)continue;
    		var opt = $('<li>', {'class':'j2scbopt j2scb-unsel', 'id': this.id() + '_opt' + this.itemCount});
    		opt[0].j2scbIndex = this.itemCount++;    		
    		this.list.append(opt);
			if (typeof item == 'string') {
				opt.text(item);
    		} else {
    			var ji = $(item);
    			ji.css("background-color", "transparent");
    			opt.append(item);
    			opt.css({height:ji.css("height")});
	    		y += opt.height();
    		}
    		this.list.css({height: (y + 2) + "px"});
	        this._on(opt, {mouseleave: '_close', mouseover: '_overOpt', click : '_clickOpt'});
    	  }
      },
      updateCSS: function() {
    	  var cbox = this.cont.parent();
    	  var font = {"font-family": cbox.css("font-family")
    			  , "font-size": cbox.css("font-size")
    			  , "font-weight": cbox.css("font-weight")
    			  , "font-style": cbox.css("font-style")
    			  }; 	  
          var w = this.element.width();
          if (w == 0)
        	  return;
          var h = this.element.height() + 'px';
          this.cont.css({
          	width: (w - 2) + 'px',
          	height: h,
          	backgroundColor: this.options.backgroundColor
          });
          this.head.css({
          	width: (w - 20) + 'px',
          	height: h,
          	backgroundColor: this.options.backgroundColor
          });
          this.head.css(font);
          this.btn.css({
          	left: (w - 20) + 'px',
          	height: h
          });
          this.btn.css(font);
          h = (this.options.height ? this.options.height + 'px' : null);
          this.popup.css({
            width: w + 'px',
        	height: h
          });  
          this.popup.css(font);
          this.list.css({
            width: w + 'px',
          	height: h,
          	overflowY: (h ? null : 'auto')
          }); 
          this.list.css(font);
      },
      
      setSelectedIndex: function(n) { return this._clickOpt({target: $('#' + this.id() + '_opt' + n)}, false) },
      _selectedItem: function() { return this.list && this.list.find('.j2scb-sel') },
            
      setText: function(s) { this.head.text(s) },
      hoverOver: function(i) {
      	this._overOpt(i >= 0 ? this.list[0].children[i] : null);  
        },
      showPopup: function() { this._open(null);},
  	  _open: function(e) {
  		this.cont.focus();
  		if (this.options.disabled)
  			return;
		this._stopT("_open");
		var loc = this.element.offset();
		if (e)
			this._trigger('change', null, [this, 'opening']);
		this.options.popupVisible = true;
	 	this.popup.css({
	 		'display':'block',
	 		left: loc.left + 'px',
        	top: (loc.top + this.element.height()) + 'px',
        	width:this.element.css('width') - 2,
	 		'z-index': this.options.zIndex
	 	});
	  	this.list.scrollTop(0);
	  	this.element.focus();
	  },
  	  hidePopup: function() {
   		 if (this.options.popupVisible) {
   			this.options.popupVisible = false;
   			this.popup.hide();
   		 }
   	  },
      _overOpt: function(e) {
    	  this._stopT("_overOpt");
    	  this.list.find('.j2scbopt').removeClass('j2scb-hov j2scb-sel');
    	  var opt = $(e && e.target || e).closest('.j2scbopt');
    	  opt.addClass('j2scb-hov');
    	  this.options.hoveredIndex = (opt[0] ? opt[0].j2scbIndex : -1);
      },
      _clickOpt: function(e, andTrigger) {
    	    andTrigger |= (arguments.length == 1);
    	    var opt = $(e.target || e).closest('.j2scbopt');
    	  	var opts = this.list.find('.j2scbopt');
    	  	opts.removeClass('j2scb-hov');
	    	if (this.options.mode=='s') {
	    		opts.removeClass('j2scb-sel');
	    	    opts.addClass('j2scb-unsel');
	    	    opt.removeClass('j2scb-unsel');
	    	    opt.addClass('j2scb-sel');
	    	    if (andTrigger)
	    	    	this._close();
	    	} else if (mode == 'm') {  
	    		  if (opt.is('.j2scb-sel')) {
	    			opt.addClass('j2scb-unsel');
	    			opt.removeClass('j2scb-sel');
	    	      } else {
	    			opt.addClass('j2scb-sel');
	    			opt.removeClass('j2scb-unsel');
	    	      }  
	    	}  
	    	this.update(andTrigger);
	    	return opt;
      },
      _stopT: function(why) {
    	  clearTimeout(this.t);
    	  this.t = 0;
      },
      _close: function() {
          if (this.t)return;
          var me = this;
          this.t = setTimeout(function() {  
      		  me.hidePopup();
      		  me.t = 0;
      	  },CLOSE_DELAY);
      }
      
    });
 
} );


