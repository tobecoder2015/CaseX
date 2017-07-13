// fields definition
var tableColumns = [
    {
        name: 'date',
        title: '日期',
    },
    {
        name: 'task_count',
        title: '任务数量',
    },
    {
        name: 'pic_count',
        title: '处理图片数',
    },
    {
        name: 'cost_per_task',
        title: '单个任务耗时(秒)',
        callback: 'formatFloat'
    },
    {
        name: 'cost_per_pic',
        title: '单张图片耗时(秒)',
        callback: 'formatFloat'
    }
]


v = new Vue({
    el: '#app',
    data: {
        fields: tableColumns,
        product: '',
        host: '',
        startTime: moment().subtract(7, 'days').valueOf(),
        endTime: +moment(),
        moreParams: [
            'start_time=' + moment().subtract(7, 'days').startOf('day').unix(),
            'end_time=' + moment().endOf('day').unix(),
        ]
    },
    methods:{
        setFilter: function() {
            this.moreParams = [
                'start_time=' + moment(this.startTime).startOf('day').unix(),
                'end_time=' + moment(this.endTime).endOf('day').unix(),
                'product=' + this.product,
                'host=' + this.host
            ]
            this.$nextTick(function() {
                this.$broadcast('vuetable:refresh')
            })
        },
        rowClassCB: function(data, index) {
            return (index % 2) === 0 ? 'positive' : ''
        },
        formatFloat: function (value) {
            return value.toFixed(2);
        },
        draw: function (data) {
            var countChart = echarts.init(document.getElementById('countChart'));
            countChart.setOption({
                title: {
                 text: '数量'
                 },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data:['任务数量', '处理图片数']
                },
                xAxis: {
                    data: data.map(function (item) {
                        return item.date
                    })
                },
                yAxis: [{
                    name: '任务数(个)'
                }, {
                    name: '处理图片数(张)'
                }],
                series: [{
                    name: '任务数量',
                    type: 'line',
                    data: data.map(function (item) {
                        return item.task_count
                    })
                }, {
                    name: '处理图片数',
                    type: 'line',
                    yAxisIndex:1,
                    data: data.map(function (item) {
                        return item.pic_count
                    })
                }]
            });

            var costChart = echarts.init(document.getElementById('costChart'));
            costChart.setOption({
                title: {
                 text: '耗时'
                 },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data:['单个任务耗时', '单张图片耗时']
                },
                xAxis: {
                    data: data.map(function (item) {
                        return item.date
                    })
                },
                yAxis: [{
                    name: '单个任务耗时(分钟)'
                }, {
                    name: '单张图片耗时(秒)'
                }],
                series: [ {
                    name: '单个任务耗时',
                    type: 'line',
                    data: data.map(function (item) {
                        return (item.cost_per_task / 60).toFixed(2)
                    })
                }, {
                    name: '单张图片耗时',
                    type: 'line',
                    yAxisIndex:1,
                    data: data.map(function (item) {
                        return item.cost_per_pic.toFixed(2)
                    })
                }]
            })
        }
    },
    events: {
        'vuetable:load-success': function(response) {
            var data = response.data.data
            this.draw(data)
        },
        'vuetable:load-error': function(response) {
            if (response.status >= 400) {
                swal('Something\'s Wrong!', response.data.message, 'error')
            } else {
                swal('失败', '加载失败', 'error')
            }
        }
    }
});