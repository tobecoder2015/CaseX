var E_SERVER_ERROR = 'Error communicating with the server'

// fields definition
var tableColumns = [
    {
        name: 'project_name',
        title: '项目',
    },
    {
        name: 'module_name',
        title: '模块',
    },
    {
        name: 'group_name',
        title: '分组',
    },
    {
        name: 'case_name',
        title: '用例',
    },
    {
        name: 'case_desc',
        title: '备注',
    },
    {
        name: 'case_author',
        title: '创建者',
    },
    {
        name: 'case_level',
        title: '等级',
        callback: 'formatStatus'
    },
    {
        name: 'case_type',
        title: '类型',
        callback: 'formatStatus2'
    },
    {
        name: 'active',
        title: '启用',
        callback: 'formatBool'
    },
    {
        name: 'parallel',
        title: '并行',
        callback: 'formatBool'
    },
    {
        name: 'update_time',
        title: '更新时间',
        sortField: 'updateTime',
        callback: 'formatDate'
    },
    {
        name: '__component:custom-action',
        dataClass: 'text-center',
    }
]

Vue.config.debug = true;

function clone(obj){
    var result={};
    for(key in obj){
        result[key]=obj[key];
    }
    return result;
}


//要在vue 实例之前定义
Vue.component('custom-action', {
    template: [
        '<div>',
        '<button @click="openDialog(rowData)" class="btn btn-default" data-toggle="modal" data-target="#detialDialog">查看</button>',
        '<button @click="edit(rowData)" class="btn btn-default" data-toggle="modal" data-target="#editDialog">编辑</button>',
        '<button @click="del(rowData)" class="btn btn-default" >删除</button>',
        '</div>'
    ].join(''),
    props: {
        rowData: {
            type: Object,
            required: true
        }
    },
    methods: {
        openDialog: function (data) {
            v.editCase=data;
        },
        edit: function (data) {
            v.editCase=clone(data);
        },

        del: function (data) {
            swal({
                title: "确认",
                text: "确定要删除该条用例吗?",
                type: "warning",
                showCancelButton: true,
                cancelButtonText: '取消',
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                closeOnConfirm: false
            }, function () {
                v.$http.post('cases/del',{caseId: data.id}, {emulateJSON: true}).then((response) => {
                    if (response.data.success > 0) {
                    swal({title:'成功', text: '成功删除用例' + data.case_name }, function() {
                        v.$broadcast('vuetable:refresh')
                    })
                } else {
                    swal('囧', '删除失败'+response.data.message)
                }

            }, (response) => {
                    swal('失败', response.data.message)
                })
            });
        }

    }
})

// var edit=document.querySelector("#editDialog");
// var editDialog=new modal(edit); //editModel.show()，显示editModel.hide()。隐藏



v = new Vue({
    el: '#app',
    data: {
        projectName: '',
        moduleName: '',
        groupName: '',
        caseAuthor:'',
        caseLevel: -1,
        fields: tableColumns,
        sortOrder: [{
            field: 'update_time',
            direction: 'desc'
        }],
        perPage: 20,
        paginationComponent: 'vuetable-pagination',
        moreParams: [],
        row: {},
        editCase: {},
        addCase: {}
    },
    watch: {
        'perPage': function(val, oldVal) {
            this.$broadcast('vuetable:refresh')
        },
        'paginationComponent': function(val, oldVal) {
            this.$broadcast('vuetable:load-success', this.$refs.vuetable.tablePagination)
            this.paginationConfig(this.paginationComponent)
        }
    },
    methods: {
        save: function () {
            swal({
                title: "确认",
                text: "确定要修改该条用例吗?",
                type: "warning",
                showCancelButton: true,
                cancelButtonText: '取消',
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                closeOnConfirm: false
            }, function () {
                v.$http.post('cases/update',{editCase: JSON.stringify(v.editCase)},{emulateJSON: true}).then((response) => {
                    if (response.data.success==true) {
                    swal({title:'成功', text: '成功修改用例' + v.editCase.case_name}, function() {
                        v.$broadcast('vuetable:refresh')
                        v.row=v.editCase;
                    })
                }else {
                    swal('囧', '修改失败'+response.data.message)
                }

            }, (response) => {
                    swal('失败', response.data.message)
                })
            });
        },
        addCase: function () {
            console.log('addCase');
            swal({
                title: "确认",
                text: "确定要保存该条用例吗?",
                type: "warning",
                showCancelButton: true,
                cancelButtonText: '取消',
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                closeOnConfirm: false
            }, function () {
                v.$http.post('cases/add',{addCase: JSON.stringify(v.addCase)},{emulateJSON: true}).then((response) => {
                    if (response.data.success==true) {
                    swal({title:'成功', text: '成功保存用例' + v.addCase.case_name}, function() {
                        v.$broadcast('vuetable:refresh')
                    })
                }else {
                    swal('囧', '保存失败'+response.data.message)
                }

            }, (response) => {
                    swal('失败', response.data.message)
                })
            });
        },
        add: function () {
            v.addCase.case_name='test';
            console.log('add case')
        },
        formatStatus: function (value) {
            if (value == 0) return '冒烟'
            else if (value == 1) return '核心'
            else if (value == 2) return '一般'
            else if (value == 3) return '详细'
            return ''
        },
        formatStatus2: function (value) {
            if (value == 0) return '接口用例'
            else if (value == 1) return '流程用例'
            return ''
        },
        formatBool: function (value) {
            if (value == true) return '是'
            else if (value == false) return '否'
            return ''
        },
        formatDate: function(value) {
            if (value == null) return ''
            value=value/1000;
            return moment.unix(value).format('YYYY-MM-DD HH:mm:ss')
        },

        /**
         * Other functions
         */
        setFilter: function() {
            this.moreParams = [
                'projectName=' + this.projectName,
                'moduleName=' + this.moduleName,
                'groupName=' + this.groupName,
                'caseLevel=' + this.caseLevel,
                'caseAuthor=' + this.caseAuthor
            ]
            this.$nextTick(function() {
                this.$broadcast('vuetable:refresh')
            })
        },
        rowClassCB: function(data, index) {
            return (index % 2) === 0 ? 'positive' : ''
        },
        paginationConfig: function(componentName) {
            console.log('paginationConfig: ', componentName)
            if (componentName == 'vuetable-pagination') {
                this.$broadcast('vuetable-pagination:set-options', {
                    wrapperClass: 'pagination',
                    icons: { first: '', prev: '', next: '', last: ''},
                    activeClass: 'active',
                    linkClass: 'btn btn-default',
                    pageClass: 'btn btn-default'
                })
            }
            if (componentName == 'vuetable-pagination-dropdown') {
                this.$broadcast('vuetable-pagination:set-options', {
                    wrapperClass: 'form-inline',
                    dropdownClass: 'form-control'
                })
            }
        }
    },
    events: {
        'vuetable:row-changed': function(data) {
            console.log('row-changed:', data.name)
        },
        'vuetable:row-clicked': function(data, event) {
            console.log('row-clicked:', data.name)
        },
        'vuetable:cell-clicked': function(data, field, event) {
            console.log('cell-clicked:', field.name)
        },
        'vuetable:load-success': function(response) {
            var data = response.data.data
            console.log(data)
        },
        'vuetable:load-error': function(response) {
            if (response.status >= 400) {
                swal('Something\'s Wrong!', response.data.message, 'error')
            } else {
                swal('Oops', E_SERVER_ERROR, 'error')
            }
        }
    }
})