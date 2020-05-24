//生成菜单
var menuItem = Vue.extend({
    name: 'menu-item',
    props:{item:{},index:0},
    template:[
        '<li :class="{active: (item.type===0 && index === 0)}">',
        '<a v-if="item.type === 0" href="javascript:;">',
        '<i v-if="item.icon != null" :class="item.icon"></i>',
        '<span>{{this.$t(item.name)}}</span>',
        '<i class="fa fa-angle-left pull-right"></i>',
        '</a>',
        '<ul v-if="item.type === 0" class="treeview-menu">',
        '<menu-item :item="item" :index="index" v-for="(item, index) in item.list" ></menu-item>',
        '</ul>',
        '<a v-if="item.type === 1" :href="\'#\'+item.url">' +
        '<i v-if="item.icon != null" :class="item.icon" onclick="reloand()"></i>' +
        '<i v-else class="fa fa-circle-o"></i>{{this.$t(item.name)}}' +
        '</a>',
        '</li>'
    ].join('')
});

//iframe自适应
$(window).on('resize', function() {
	var $content = $('.content');
	$content.height($(this).height() - 120);
	$content.find('iframe').each(function() {
		$(this).height($content.height());
	});
}).resize();

//注册菜单组件
Vue.component('menuItem',menuItem);


 
var vm = new Vue({
	el:'#rapp',
	provide() { // 注册一个方法
		return {
			reload: this.reload
		}
	},
	i18n,
	data:{
		user:{},
		menuList:{},
		main:"main.html",
		password:'',
		newPassword:'',
        navTitle:"welcome",
        language:$.cookie('locale_language') == undefined ? 'zh':$.cookie('locale_language'),
	},
	methods: {
		getMenuList: function () {
			$.getJSON(baseURL + "/sys/menu/nav", function(r){
				vm.menuList = r.menuList;
                window.permissions = r.permissions;
			});
		},
		getUser: function(){
			$.getJSON(baseURL + "/sys/user/info", function(r){
				vm.user = r.user;
			});
		},
		changeLanuage: function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.$t('sys.changelanguage'),
				area: ['550px', '270px'],
				shadeClose: false,
				content: jQuery("#languageLayer"),
				btn: [vm.$t('sys.menu.confirm'), vm.$t('sys.menu.cancel')],
				btn1: function (index) {
					$.cookie('locale_language', vm.language, { expires: 1, path: '/' })
					vm.$i18n.locale = vm.language;
					layer.close(index);
				}
			});
		},
		updatePassword: function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.$t('sys.changepass'),
				area: ['550px', '270px'],
				shadeClose: false,
				content: jQuery("#passwordLayer"),
				btn: [vm.$t('sys.menu.confirm'), vm.$t('sys.menu.cancel')],
				btn1: function (index) {
					var data = "password="+vm.password+"&newPassword="+vm.newPassword;
					$.ajax({
						type: "POST",
					    url: baseURL + "/sys/user/updatePassword",
					    data: data,
					    dataType: "json",
					    success: function(r){
							if(r.code == 0){
								layer.close(index);
								layer.alert(vm.$t('sys.menu.success'), function(){
									vm.logout();
								});
							}else{
								layer.alert(r.msg);
							}
						}
					});
	            }
			});
		},
        logout: function () {
            $.ajax({
                type: "POST",
                url: baseURL + "/sys/logout",
                dataType: "json",
                success: function(r){
                    //删除本地token
                    localStorage.removeItem("X-Token");
                    //跳转到登录页面
                    location.href = baseURL + '/login.html';
                }
            });
        },
        goback:function(){
        	vm.main = '';
    	},
//    	initLanguage:function(){
//            $.cookie('locale_language', 'ko', { expires: 1, path: '/' });
////            vm.$i18n.locale = l;
//        }
	},
	created: function(){
		this.getMenuList();
		this.getUser();
//		this.initLanguage();
	},
	updated: function(){
		//路由
		var router = new Router();
		routerList(router, vm.menuList);
		router.start();
	}
});

function routerList(router, menuList){
	for(var key in menuList){
		var menu = menuList[key];
		if(menu.type == 0){
			routerList(router, menu.list);
		}else if(menu.type == 1){
			router.add('#'+menu.url, function() {
				var url = window.location.hash;
				//替换iframe的url
			    vm.main = url.replace('#', '');
			    
			    //导航菜单展开
			    $(".treeview-menu li").removeClass("active");
                $(".sidebar-menu li").removeClass("active");
			    $("a[href='"+url+"']").parents("li").addClass("active");
			    vm.navTitle = $("a[href='"+url+"']").text();
			});
		}
	}
}
