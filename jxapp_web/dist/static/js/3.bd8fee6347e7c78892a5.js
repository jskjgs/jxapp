webpackJsonp([3],{291:function(e,t,n){n(387);var r=n(19)(n(347),n(417),null,null);e.exports=r.exports},294:function(e,t,n){var r=n(7),a=n(3),o=n(54),i=n(295),s=n(11).f;e.exports=function(e){var t=a.Symbol||(a.Symbol=o?{}:r.Symbol||{});"_"==e.charAt(0)||e in t||s(t,e,{value:i.f(e)})}},295:function(e,t,n){t.f=n(5)},296:function(e,t,n){var r=n(118),a=n(58).concat("length","prototype");t.f=Object.getOwnPropertyNames||function(e){return r(e,a)}},297:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}t.__esModule=!0;var a=n(301),o=r(a),i=n(300),s=r(i),l="function"==typeof s.default&&"symbol"==typeof o.default?function(e){return typeof e}:function(e){return e&&"function"==typeof s.default&&e.constructor===s.default&&e!==s.default.prototype?"symbol":typeof e};t.default="function"==typeof s.default&&"symbol"===l(o.default)?function(e){return void 0===e?"undefined":l(e)}:function(e){return e&&"function"==typeof s.default&&e.constructor===s.default&&e!==s.default.prototype?"symbol":void 0===e?"undefined":l(e)}},298:function(e,t,n){n(314);var r=n(19)(n(299),null,null,null);e.exports=r.exports},299:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n(114),a=n.n(r),o=n(297),i=n.n(o),s=n(57),l=n.n(s),u=n(6),c=n.n(u),p=n(115),f=n.n(p),d=n(56),h=n.n(d);t.default={name:"SearchTable",props:{initData:{type:Array},listApi:{type:Object,default:function(){return{requestFn:function(){return h.a.reject()},responseFn:function(){}}}},apiKeysMap:{type:Object},tableAttrs:{type:Object},columnData:{type:Array,default:function(){return[]}}},created:function(){this.initData||this.init()},data:function(){return{loading:!1,currentPage:1,pageSize:10,total:0,tableData:[]}},watch:{currentPage:function(e){console.log("newPageNum",e),this.getList(f()({},this.apiKeys.currentPage,e))},listQueryParams:function(e,t){var n=this.apiKeys.currentPage;e[n]===t[n]&&this.init()}},computed:{apiKeys:function(){return c()({},{currentPage:"pageNum",pageSize:"pageSize"},this.apiKeysMap)},listQueryParams:function(){var e=this,t=this.apiKeys||{},n={};return l()(t).forEach(function(r){var a=t[r];"object"===(void 0===a?"undefined":i()(a))?(a.innerKey&&(e[a.innerKey]=a.value),n[r]=a.value):n[a]=e[r]}),n}},methods:{init:function(){return this.currentPage=1,this.getList(f()({},this.apiKeys.currentPage,1))},getList:function(e){var t=this;this.getListId=this.getListId||0;var n=++this.getListId;return this.loading=!0,this.listApi.requestFn(c()({},this.listQueryParams,e)).then(function(e){n===t.getListId&&t.listApi.responseFn.call(t,e)}).finally(function(){n===t.getListId&&(t.loading=!1)})},handlePageChange:function(e){this.currentPage=e}},render:function(e){var t=this,n=function(e){return{formatter:function(t,n){return t[e]||"--"},"show-overflow-tooltip":!0}};return e("div",{class:"search-table"},[this.$slots["table-tools"],e("el-table",a()([this.tableAttrs,{attrs:{data:this.initData||this.tableData}},{directives:[{name:"loading",value:this.loading}]}]),[this.columnData.map(function(r){return r.slotName?t.$slots[r.slotName]:e("el-table-column",a()([{attrs:{align:t.tableAttrs.props?t.tableAttrs.props.align:"left"}},{props:c()({},n(r.attrs.prop),r.attrs)},{scopedSlots:r.scopedSlots}]),[])}),this.$slots.default]),this.total>this.pageSize&&e("div",{class:"pagination-wrap"},[e("el-pagination",a()([{style:"text-align: right; margin-top: 30px",attrs:{"current-page":this.currentPage}},{on:{"current-change":this.handlePageChange}},{attrs:{"page-size":this.pageSize,layout:"prev, pager, next, jumper",total:this.total}}]),[])])])}}},300:function(e,t,n){e.exports={default:n(302),__esModule:!0}},301:function(e,t,n){e.exports={default:n(303),__esModule:!0}},302:function(e,t,n){n(310),n(120),n(311),n(312),e.exports=n(3).Symbol},303:function(e,t,n){n(60),n(121),e.exports=n(295).f("iterator")},304:function(e,t,n){var r=n(30),a=n(112),o=n(111);e.exports=function(e){var t=r(e),n=a.f;if(n)for(var i,s=n(e),l=o.f,u=0;s.length>u;)l.call(e,i=s[u++])&&t.push(i);return t}},305:function(e,t,n){var r=n(22);e.exports=Array.isArray||function(e){return"Array"==r(e)}},306:function(e,t,n){var r=n(30),a=n(29);e.exports=function(e,t){for(var n,o=a(e),i=r(o),s=i.length,l=0;s>l;)if(o[n=i[l++]]===t)return n}},307:function(e,t,n){var r=n(55)("meta"),a=n(23),o=n(20),i=n(11).f,s=0,l=Object.isExtensible||function(){return!0},u=!n(21)(function(){return l(Object.preventExtensions({}))}),c=function(e){i(e,r,{value:{i:"O"+ ++s,w:{}}})},p=function(e,t){if(!a(e))return"symbol"==typeof e?e:("string"==typeof e?"S":"P")+e;if(!o(e,r)){if(!l(e))return"F";if(!t)return"E";c(e)}return e[r].i},f=function(e,t){if(!o(e,r)){if(!l(e))return!0;if(!t)return!1;c(e)}return e[r].w},d=function(e){return u&&h.NEED&&l(e)&&!o(e,r)&&c(e),e},h=e.exports={KEY:r,NEED:!1,fastKey:p,getWeak:f,onFreeze:d}},308:function(e,t,n){var r=n(111),a=n(31),o=n(29),i=n(113),s=n(20),l=n(116),u=Object.getOwnPropertyDescriptor;t.f=n(12)?u:function(e,t){if(e=o(e),t=i(t,!0),l)try{return u(e,t)}catch(e){}if(s(e,t))return a(!r.f.call(e,t),e[t])}},309:function(e,t,n){var r=n(29),a=n(296).f,o={}.toString,i="object"==typeof window&&window&&Object.getOwnPropertyNames?Object.getOwnPropertyNames(window):[],s=function(e){try{return a(e)}catch(e){return i.slice()}};e.exports.f=function(e){return i&&"[object Window]"==o.call(e)?s(e):a(r(e))}},310:function(e,t,n){"use strict";var r=n(7),a=n(20),o=n(12),i=n(8),s=n(119),l=n(307).KEY,u=n(21),c=n(59),p=n(32),f=n(55),d=n(5),h=n(295),b=n(294),v=n(306),m=n(304),g=n(305),A=n(13),y=n(29),C=n(113),x=n(31),w=n(117),_=n(309),S=n(308),D=n(11),P=n(30),k=S.f,j=D.f,O=_.f,B=r.Symbol,K=r.JSON,E=K&&K.stringify,I=d("_hidden"),M=d("toPrimitive"),T={}.propertyIsEnumerable,N=c("symbol-registry"),F=c("symbols"),L=c("op-symbols"),z=Object.prototype,V="function"==typeof B,$=r.QObject,R=!$||!$.prototype||!$.prototype.findChild,Y=o&&u(function(){return 7!=w(j({},"a",{get:function(){return j(this,"a",{value:7}).a}})).a})?function(e,t,n){var r=k(z,t);r&&delete z[t],j(e,t,n),r&&e!==z&&j(z,t,r)}:j,q=function(e){var t=F[e]=w(B.prototype);return t._k=e,t},U=V&&"symbol"==typeof B.iterator?function(e){return"symbol"==typeof e}:function(e){return e instanceof B},W=function(e,t,n){return e===z&&W(L,t,n),A(e),t=C(t,!0),A(n),a(F,t)?(n.enumerable?(a(e,I)&&e[I][t]&&(e[I][t]=!1),n=w(n,{enumerable:x(0,!1)})):(a(e,I)||j(e,I,x(1,{})),e[I][t]=!0),Y(e,t,n)):j(e,t,n)},J=function(e,t){A(e);for(var n,r=m(t=y(t)),a=0,o=r.length;o>a;)W(e,n=r[a++],t[n]);return e},Q=function(e,t){return void 0===t?w(e):J(w(e),t)},H=function(e){var t=T.call(this,e=C(e,!0));return!(this===z&&a(F,e)&&!a(L,e))&&(!(t||!a(this,e)||!a(F,e)||a(this,I)&&this[I][e])||t)},Z=function(e,t){if(e=y(e),t=C(t,!0),e!==z||!a(F,t)||a(L,t)){var n=k(e,t);return!n||!a(F,t)||a(e,I)&&e[I][t]||(n.enumerable=!0),n}},G=function(e){for(var t,n=O(y(e)),r=[],o=0;n.length>o;)a(F,t=n[o++])||t==I||t==l||r.push(t);return r},X=function(e){for(var t,n=e===z,r=O(n?L:y(e)),o=[],i=0;r.length>i;)!a(F,t=r[i++])||n&&!a(z,t)||o.push(F[t]);return o};V||(B=function(){if(this instanceof B)throw TypeError("Symbol is not a constructor!");var e=f(arguments.length>0?arguments[0]:void 0),t=function(n){this===z&&t.call(L,n),a(this,I)&&a(this[I],e)&&(this[I][e]=!1),Y(this,e,x(1,n))};return o&&R&&Y(z,e,{configurable:!0,set:t}),q(e)},s(B.prototype,"toString",function(){return this._k}),S.f=Z,D.f=W,n(296).f=_.f=G,n(111).f=H,n(112).f=X,o&&!n(54)&&s(z,"propertyIsEnumerable",H,!0),h.f=function(e){return q(d(e))}),i(i.G+i.W+i.F*!V,{Symbol:B});for(var ee="hasInstance,isConcatSpreadable,iterator,match,replace,search,species,split,toPrimitive,toStringTag,unscopables".split(","),te=0;ee.length>te;)d(ee[te++]);for(var ee=P(d.store),te=0;ee.length>te;)b(ee[te++]);i(i.S+i.F*!V,"Symbol",{for:function(e){return a(N,e+="")?N[e]:N[e]=B(e)},keyFor:function(e){if(U(e))return v(N,e);throw TypeError(e+" is not a symbol!")},useSetter:function(){R=!0},useSimple:function(){R=!1}}),i(i.S+i.F*!V,"Object",{create:Q,defineProperty:W,defineProperties:J,getOwnPropertyDescriptor:Z,getOwnPropertyNames:G,getOwnPropertySymbols:X}),K&&i(i.S+i.F*(!V||u(function(){var e=B();return"[null]"!=E([e])||"{}"!=E({a:e})||"{}"!=E(Object(e))})),"JSON",{stringify:function(e){if(void 0!==e&&!U(e)){for(var t,n,r=[e],a=1;arguments.length>a;)r.push(arguments[a++]);return t=r[1],"function"==typeof t&&(n=t),!n&&g(t)||(t=function(e,t){if(n&&(t=n.call(this,e,t)),!U(t))return t}),r[1]=t,E.apply(K,r)}}}),B.prototype[M]||n(14)(B.prototype,M,B.prototype.valueOf),p(B,"Symbol"),p(Math,"Math",!0),p(r.JSON,"JSON",!0)},311:function(e,t,n){n(294)("asyncIterator")},312:function(e,t,n){n(294)("observable")},313:function(e,t,n){t=e.exports=n(282)(),t.push([e.i,".search-table .table-tools{margin-top:30px}.search-table .table-tools .tool-item+.tool-item{margin-left:20px}.search-table .table-tools .el-button{width:80px;border-radius:18px}.search-table .el-table{margin-top:20px}","",{version:3,sources:["/Users/zhengjitf/Documents/programs/积拾/jxapp/jxapp_web/src/components/_common/searchTable/SearchTable.vue"],names:[],mappings:"AACA,2BACE,eAAiB,CAClB,AACD,iDACI,gBAAkB,CACrB,AACD,sCACI,WAAY,AACZ,kBAAoB,CACvB,AACD,wBACE,eAAiB,CAClB",file:"SearchTable.vue",sourcesContent:["\n.search-table .table-tools {\n  margin-top: 30px;\n}\n.search-table .table-tools .tool-item + .tool-item {\n    margin-left: 20px;\n}\n.search-table .table-tools .el-button {\n    width: 80px;\n    border-radius: 18px;\n}\n.search-table .el-table {\n  margin-top: 20px;\n}\n"],sourceRoot:""}])},314:function(e,t,n){var r=n(313);"string"==typeof r&&(r=[[e.i,r,""]]),r.locals&&(e.exports=r.locals);n(283)("58c3b7de",r,!0)},318:function(e,t,n){"use strict";n.d(t,"b",function(){return a}),n.d(t,"a",function(){return o}),n.d(t,"c",function(){return i}),n.d(t,"d",function(){return s});var r=n(16),a=function(e){return n.i(r.b)({url:"/department/queryAllDepartment",type:"get",params:e})},o=function(e){return n.i(r.b)({url:"/doctor_i/queryAllDoctor",type:"get",params:e})},i=function(e,t){return n.i(r.b)({url:"/doctor_i/modifyDoctor",type:"post",params:e,data:t})},s=function(e){return n.i(r.b)({url:"/doctor_i/topDoctor",type:"post",data:e})}},333:function(e,t,n){"use strict";n.d(t,"a",function(){return a});var r=n(16),a=function(e){return n.i(r.b)({url:"/register/queryRegisterAdmin",type:"get",params:e})}},334:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"HeaderSearchSelect",props:{label:{type:String},width:{type:Number,default:200},placeholder:{type:String,default:"请选择"},options:{type:Array,default:function(){return[]}},value:{type:null}},data:function(){return{popoverVisible:!1}},computed:{selectVal:{get:function(){return this.value},set:function(e){this.popoverVisible=!1,this.$emit("input",e)}}},methods:{handlePopoverVisibleChange:function(e){this.$emit("visibleChange",e)}}}},347:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n(6),a=n.n(r),o=n(298),i=n.n(o),s=n(393),l=n.n(s),u=n(318),c=n(333),p=n(16);t.default={name:"Diary",components:{SearchTable:i.a},data:function(){var e=(this.$createElement,this),t=[{label:"全部",value:void 0},{label:"过期未到诊",value:1},{label:"正常就诊",value:0},{label:"预约就诊",value:2}],r={0:"正常就诊",1:"过期未到诊",2:"预约就诊"};return this.tableAttrs={props:{"tooltip-effect":"dark",style:"width: 100%",align:"center"}},this.columnData=[{attrs:{prop:"registerTime",label:"预约时间","min-width":"180",formatter:function(e,t){return e.registerTime?n.i(p.d)(e.registerTime,"Y-M-D h:m"):"--"}}},{attrs:{prop:"patientName",label:"客户姓名","min-width":"120"}},{attrs:{prop:"idCard",label:"就诊卡号","min-width":"120"}},{attrs:{prop:"doctorName",label:"医生姓名","min-width":"120","render-header":function(t,r){r.column,r.$index;return t(l.a,{props:{label:"医生姓名",options:e.doctorList,value:e.apiKeysMap.doctorId.value},on:{input:function(t){e.apiKeysMap.doctorId.value=t},visibleChange:function(t){t&&n.i(u.a)().then(function(t){var n=t.content||{},r=(n.list||[]).map(function(e){var t=e.doctor||{};return{label:t.name,value:t.id}});e.doctorList=[{label:"全部",value:void 0}].concat(r)})}}})}}},{attrs:{prop:"department",label:"科室","min-width":"120","render-header":function(t,r){r.column,r.$index;return t(l.a,{props:{label:"科室",options:e.departmentList,value:e.apiKeysMap.departmentId.value},on:{input:function(t){e.apiKeysMap.departmentId.value=t},visibleChange:function(t){t&&0===e.departmentList.length&&n.i(u.b)().then(function(t){var n=(t.content||{}).map(function(e){return{label:e.name,value:e.id}});e.departmentList=[{label:"全部",value:void 0}].concat(n)})}}})}}},{attrs:{prop:"phone",label:"预留手机号","min-width":"120"}},{attrs:{prop:"idCard",label:"身份证号","min-width":"160"}},{attrs:{prop:"status",label:"状态","min-width":"120",formatter:function(e,t){return r[e.status]},"render-header":function(n,r){r.column,r.$index;return n("el-dropdown",null,[n("span",{class:"el-dropdown-link"},["状态",n("i",{class:"el-icon-arrow-down el-icon--right",style:"cursor: pointer;"},[])]),n("el-dropdown-menu",{slot:"dropdown",class:"log-status-drodown-menu"},[t.map(function(t){return n("el-dropdown-item",{class:{"status-active":t.value===(e.apiKeysMap&&e.apiKeysMap.status.value)},nativeOn:{click:function(){return e.selectStatus(t)}}},[t.label])})])])}}}],this.listApi={requestFn:c.a,responseFn:function(e){var t=e.content||{};this.tableData=t.list||[],this.total=t.total||0}},this.checkOptions=[{label:"审核通过",value:"0"},{label:"审核拒绝",value:"2"}],{apiKeysMap:{key:{value:void 0},status:{value:void 0},startTime:{value:void 0},endTime:{value:void 0},doctorId:{value:void 0},departmentId:{value:void 0},currentPage:"startPage"},createTimeRange:[],searchKeyword:"",doctorList:[],departmentList:[]}},methods:{selectStatus:function(e){this.apiKeysMap.status.value=e.value},handleSearch:function(){var e=this.createTimeRange||[];this.apiKeysMap=a()({},this.apiKeysMap,{startTime:{value:new Date(e[0]||"").getTime()||void 0},endTime:{value:new Date(e[1]||"").getTime()||void 0},key:{value:this.searchKeyword||void 0}})}}}},365:function(e,t,n){t=e.exports=n(282)(),t.push([e.i,".header-search-select .el-button--text{color:inherit}.header-search-select__popover{padding:0}.header-search-select__popover .el-select{width:100%}.header-search-select__popover .el-input__inner{border:0}","",{version:3,sources:["/Users/zhengjitf/Documents/programs/积拾/jxapp/jxapp_web/src/components/_common/headerSearchSelect/HeaderSearchSelect.vue"],names:[],mappings:"AACA,uCACE,aAAe,CAChB,AACD,+BACE,SAAW,CACZ,AACD,0CACI,UAAY,CACf,AACD,gDACI,QAAU,CACb",file:"HeaderSearchSelect.vue",sourcesContent:["\n.header-search-select .el-button--text {\n  color: inherit;\n}\n.header-search-select__popover {\n  padding: 0;\n}\n.header-search-select__popover .el-select {\n    width: 100%;\n}\n.header-search-select__popover .el-input__inner {\n    border: 0;\n}\n"],sourceRoot:""}])},368:function(e,t,n){t=e.exports=n(282)(),t.push([e.i,"#reverse-manage .status-label{display:inline-block;width:60px;height:24px;border-radius:4px;font-size:12px}#reverse-manage .status-label.status-pass{color:#10ad57;background:rgba(19,206,102,.1);border:1px solid rgba(19,206,102,.2)}#reverse-manage .status-label.status-wait{color:#20a0ff;background:rgba(32,160,255,.1);border:1px solid rgba(32,160,255,.2)}#reverse-manage .status-label.status-refuse{color:#ff4949;background:rgba(255,73,73,.1);border:1px solid rgba(255,73,73,.2)}.log-status-drodown-menu .el-dropdown-menu__item.status-active{background:#20a0ff;color:#fff}","",{version:3,sources:["/Users/zhengjitf/Documents/programs/积拾/jxapp/jxapp_web/src/components/reserve/Index.vue"],names:[],mappings:"AACA,8BACE,qBAAsB,AACtB,WAAY,AACZ,YAAa,AACb,kBAAmB,AACnB,cAAgB,CACjB,AACD,0CACI,cAAe,AACf,+BAAoC,AACpC,oCAA0C,CAC7C,AACD,0CACI,cAAe,AACf,+BAAoC,AACpC,oCAA0C,CAC7C,AACD,4CACI,cAAe,AACf,8BAAmC,AACnC,mCAAyC,CAC5C,AACD,+DACE,mBAAoB,AACpB,UAAY,CACb",file:"Index.vue",sourcesContent:["\n#reverse-manage .status-label {\n  display: inline-block;\n  width: 60px;\n  height: 24px;\n  border-radius: 4px;\n  font-size: 12px;\n}\n#reverse-manage .status-label.status-pass {\n    color: #10ad57;\n    background: rgba(19, 206, 102, 0.1);\n    border: 1px solid rgba(19, 206, 102, 0.2);\n}\n#reverse-manage .status-label.status-wait {\n    color: #20a0ff;\n    background: rgba(32, 160, 255, 0.1);\n    border: 1px solid rgba(32, 160, 255, 0.2);\n}\n#reverse-manage .status-label.status-refuse {\n    color: #ff4949;\n    background: rgba(255, 73, 73, 0.1);\n    border: 1px solid rgba(255, 73, 73, 0.2);\n}\n.log-status-drodown-menu .el-dropdown-menu__item.status-active {\n  background: #20a0ff;\n  color: #fff;\n}\n"],sourceRoot:""}])},384:function(e,t,n){var r=n(365);"string"==typeof r&&(r=[[e.i,r,""]]),r.locals&&(e.exports=r.locals);n(283)("10b51f7a",r,!0)},387:function(e,t,n){var r=n(368);"string"==typeof r&&(r=[[e.i,r,""]]),r.locals&&(e.exports=r.locals);n(283)("55001704",r,!0)},393:function(e,t,n){n(384);var r=n(19)(n(334),n(414),null,null);e.exports=r.exports},414:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"header-search-select"},[n("el-popover",{ref:"popover",attrs:{"popper-class":"header-search-select__popover",placement:"bottom",width:e.width,trigger:"click"},on:{show:function(t){e.handlePopoverVisibleChange(!0)},hide:function(t){e.handlePopoverVisibleChange(!1)}},model:{value:e.popoverVisible,callback:function(t){e.popoverVisible=t},expression:"popoverVisible"}},[n("el-select",{attrs:{filterable:"",placeholder:e.placeholder},model:{value:e.selectVal,callback:function(t){e.selectVal=t},expression:"selectVal"}},e._l(e.options,function(e){return n("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),n("el-button",{directives:[{name:"popover",rawName:"v-popover:popover",arg:"popover"}],attrs:{type:"text"}},[e._v("\n    "+e._s(e.label)),n("i",{staticClass:"el-icon-arrow-down",staticStyle:{"margin-left":"5px"}})])],1)},staticRenderFns:[]}},417:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"reverse-manage"}},[e._m(0),e._v(" "),n("search-table",{ref:"searchTable",attrs:{"table-attrs":e.tableAttrs,"column-data":e.columnData,"list-api":e.listApi,"api-keys-map":e.apiKeysMap}},[n("div",{staticClass:"table-tools flex--vcenter",slot:"table-tools"},[n("div",{staticClass:"tool-item"},[e._v("\n        预约时间：\n        "),n("el-date-picker",{staticStyle:{width:"230px"},attrs:{type:"daterange",placeholder:"选择日期范围"},model:{value:e.createTimeRange,callback:function(t){e.createTimeRange=t},expression:"createTimeRange"}})],1),e._v(" "),n("div",{staticClass:"tool-item"},[e._v("\n        搜索关键字：\n        "),n("el-input",{staticStyle:{width:"290px"},attrs:{placeholder:"请输入客户姓名／卡号..."},model:{value:e.searchKeyword,callback:function(t){e.searchKeyword=t},expression:"searchKeyword"}})],1),e._v(" "),n("div",{staticClass:"tool-item"},[n("el-button",{attrs:{type:"primary"},on:{click:e.handleSearch}},[e._v("搜索")])],1)])])],1)},staticRenderFns:[function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"flex--vcenter page-top"},[n("div",{staticClass:"page-title"},[e._v("\n      预约管理\n    ")])])}]}}});