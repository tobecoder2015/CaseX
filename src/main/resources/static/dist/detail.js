/**
 * Created by zhongyi.sun on 2016/9/28.
 */
v = new Vue({
    el: '#app',
    data: {
        request:{},
        poi:{},
        meaningless:{},
        repeat:{},
        ocr:{},
        human:{},
        curPicIndex: 1,
        curPic: '',
        onlyOcr: false,
        onlyHuman: false,
        map: null,
        marker: null,
        markers: [],
        infoWindow: null
    },
    ready: function () {
        var taskId = getParameterByName('task_id')
        if (!taskId) {
            swal('无效参数！')
            return
        }
        this.$http.get('/index/request?task_id=' + taskId).then((response) => {
            this.request = response.data
            this.curPic = this.request.pics[0]
            var self = this
            if (this.request.pois) {
                this.request.pois.forEach(function (item) {
                    self.poi[item.id] = item
                })
            }

            this.map = new AMap.Map('map', {
                resizeEnable: true,
                zoom: 20,
                forceZooms:[3,20],
                center: [this.curPic.x, this.curPic.y]
            });
            this.marker = new AMap.Marker({
                icon: new AMap.Icon({
                    image: "img/entr_r.png",
                    size : new AMap.Size(8,8)
                }),
                offset: new AMap.Pixel(-4,-4),
                position: [this.curPic.x, this.curPic.y],
                map: this.map,
                zIndex: 200
            });

            var polyline = new AMap.Polyline({
                path: this.request.pics.map(function (pic) {
                    return [pic.x, pic.y]
                }),          //设置线覆盖物路径
                strokeColor: "#3366FF", //线颜色
                strokeOpacity: 0.8,       //线透明度
                strokeWeight: 3,        //线宽
                strokeStyle: "dashed",   //线样式
                // strokeDasharray: [10, 5], //补充线样式
                map: this.map
            });

            this.$http.get('/index/result?task_id=' + taskId).then((response) => {
                var result = response.data
                var self = this
                if (result.meaningless_pics) {
                result.meaningless_pics.forEach(function (key) {
                    self.meaningless[key] = 1
                })
            }
            if (result.repeat_pics) {
                result.repeat_pics.forEach(function (key) {
                    self.repeat[key] = 1
                })
            }
            if (result.ocr_result) {
                result.ocr_result.forEach(function (item) {
                    var array = self.ocr[item.pic]
                    if (!array) {
                        array = []
                        self.ocr[item.pic] = array
                    }
                    array.push(item)
                    self.setMarker(item)
                })

            }
        })

            this.$http.get('/poiresult/detail?task_id=' + taskId).then(
                (response) => {
                    var result = response.data
                    result.forEach(function(item) {
                        var array = self.human[item.pic]
                        if (!array) {
                            array = []
                            self.human[item.pic] = array
                        }
                        array.push(item)
                    })
                }
            )
    })

        $(document).keydown(function(event){
            if(event.keyCode == 37){
                v.prev();
            }else if (event.keyCode == 39){
                v.next();
            }else if(event.keyCode == 38){
                v.prePOI();
            }else if(event.keyCode == 40){
                v.nextPOI();
            }
            else if(event.keyCode == 49){ //数字1，是
                v.isPOI();
            }
            else if(event.keyCode == 50){ //数字2，否
                v.isNotPOI();
            }
            else if(event.keyCode == 51){ //数字3，不确定
                v.noIdeaPOI();
            }
            else if(event.keyCode == 48){ //数字0，全否
                v.isAllNoPOI();
            }
            else if(event.keyCode == 57){ //数字9，全是
                v.isAllPOI();
            }
        });
    },
    methods: {
        prev: function () {
            var index = this.curPicIndex
            while (index > 0) {
                --index
                if (index == 0) break
                if (!this.onlyOcr && !this.onlyHuman) break
                if (this.onlyOcr && this.ocr[this.request.pics[index - 1].id]) break
                if (this.onlyHuman && this.human[this.request.pics[index - 1].id]) break
            }
            if (index > 0) {
                this.curPicIndex = index
            }
        },
        next: function () {
            var index = this.curPicIndex
            while (index <= this.request.pics.length) {
                ++index
                if (index > this.request.pics.length) break
                if (!this.onlyOcr && !this.onlyHuman) break
                if (this.onlyOcr && this.ocr[this.request.pics[index - 1].id]) break
                if (this.onlyHuman && this.human[this.request.pics[index - 1].id]) break
            }
            if (index <= this.request.pics.length) {
                this.curPicIndex = index
            }
        },
        nextPOI: function(){
            var focus = -1;
            var $tr = $("#table").find("tr");
            var length = $tr.length;
            $.each($tr, function(i,e){
                if($tr.find("td").find("select")[i] == document.activeElement){
                    focus = i+2;
                }
            });
            if(focus == length){
                return;
            };
            if(focus == -1){
                $("#table").find("tr").eq(1).find("td").find("select").focus();
                $("#table").find("tr").eq(2).focus();
                $("#table").find("tr").eq(1).click();
                this.showSelRect(this.ocr[this.curPic.id][0]);
            }else{
                $("#table").find("tr").eq(focus).find("td").find("select").focus();
                $("#table").find("tr").eq(focus).click();
                this.showSelRect(this.ocr[this.curPic.id][focus-1]);
            }
        },
        prePOI: function(){
            var focus = -1;
            var $tr = $("#table").find("tr");
            var length = $tr.length;
            $.each($tr, function(i,e){
                if($tr.find("td").find("select")[i] == document.activeElement){
                    focus = i;
                }
            });
            if(focus == 0){
                return;
            };
            if(focus == -1){
                $("#table").find("tr").eq(length-1).find("td").find("select").focus();
                $("#table").find("tr").eq(length).focus();
                $("#table").find("tr").eq(length).click();
                this.showSelRect(this.ocr[this.curPic.id][length-2]);
            }else{
                $("#table").find("tr").eq(focus).find("td").find("select").focus();
                $("#table").find("tr").eq(focus).click();
                this.showSelRect(this.ocr[this.curPic.id][focus-1]);
            }
        },
        isPOI:function(){
            var $tr = $("#table").find("tr");
            var focus = -1;
            $.each($tr, function(i,e){
                if($tr.find("td").find("select")[i] == document.activeElement){
                    focus = i+1;
                }
            });
            if(focus == -1)
                return false;
            else{
                $("#table").find("tr").eq(focus).find("td").find("select")[0].options[1].selected=true;
                this.updatePoi(this.ocr[this.curPic.id][focus-1],focus-1);
            }

        },
        isNotPOI:function(){
            var $tr = $("#table").find("tr");
            var focus = -1;
            $.each($tr, function(i,e){
                if($tr.find("td").find("select")[i] == document.activeElement){
                    focus = i+1;
                }
            });
            if(focus == -1)
                return false;
            else{
                $("#table").find("tr").eq(focus).find("td").find("select")[0].options[2].selected=true;
                this.updatePoi(this.ocr[this.curPic.id][focus-1],focus-1);
            }

        },
        noIdeaPOI:function(){
            var $tr = $("#table").find("tr");
            var focus = -1;
            $.each($tr, function(i,e){
                if($tr.find("td").find("select")[i] == document.activeElement){
                    focus = i+1;
                }
            });
            if(focus == -1)
                return false;
            else{
                $("#table").find("tr").eq(focus).find("td").find("select")[0].options[3].selected=true;
                this.updatePoi(this.ocr[this.curPic.id][focus-1],focus-1);
            }
        },
        isAllNoPOI:function(){
            this.updatePicPois(0);
        },
        isAllPOI:function(){
            this.updatePicPois(1);
        },
        selectEvent: function(){
            event.returnValue=false;
            if(event.keyCode == 38){
                return false;
            }else if(event.keyCode == 40){
                return false;
            }
        },
        showInfoWindow: function (result) {
            if (result.poiid) {
                var p = this.poi[result.poiid]
                this.infoWindow = new AMap.InfoWindow({
                    content: p.id + " " + p.name + '<br/>' + p.x + ',' + p.y
                })
                this.infoWindow.open(this.map, [p.x, p.y]);
            } else if (result.location) {
                this.infoWindow = new AMap.InfoWindow({
                    content: result.ocr_text + '<br/>' + result.location
                })
                this.infoWindow.open(this.map, result.location.split(","));
            }
        },
        showSelRect: function (result) {
            this.clearRect();
            var selpoi = [];
            selpoi.push(result);
            this.drawRect(selpoi);
        },
        showRect: function (result) {
            this.clearRect();
            this.drawRect(result);
        },
        updatePoi: function (item,index){
            var ispoi = $(".isPoi_"+index).val();
            this.$http.get('/poidetail/update?taskId=' + getParameterByName('task_id') + "&ocr_text=" + encodeURIComponent(item.ocr_text) + "&pic=" + item.pic + "&polygon=" + item.polygon + "&is_poi=" + parseInt(ispoi)).then(
                (response) => {
                    this.ocr[item.pic][index].is_poi = ispoi;
                }
            )
        },
        updatePicPois: function (ispoi){
            var ocr = this.ocr[this.curPic.id]
            if(ispoi == null){
                $.each(ocr,function(i,e){
                    ocr[i].is_poi = $(".isPoi_"+i).val();

                });
            }else{
                $.each(ocr,function(i,e){
                    ocr[i].is_poi = ispoi;
                })
            }
            var data = {};
            data['taskId'] = getParameterByName('task_id');
            data['pois'] = JSON.stringify(ocr);
            $.post('/poidetail/updatepois',data,function(){
                var $select = $("#table").find("tr").find("td").find("select");
                $.each($select, function(i,e){
                    $select[i].value=ispoi;
                });
            })

        },
        setMarker: function (item) {
            if (item.location) {
                this.markers.push(new AMap.Marker({
                    map : this.map,
                    offset : new AMap.Pixel(-5,-5),
                    icon : new AMap.Icon({
                        size: new AMap.Size(10, 10),
                        image: "img/green.png"
                    }),
                    position : item.location.split(','),
                    title: item.ocr_text,
                    zIndex: 205
                }))
            }
            if (item.poiid) {
                var p = this.poi[item.poiid]
                this.markers.push(new AMap.Marker({
                    map : this.map,
                    offset : new AMap.Pixel(-5,-5),
                    icon : new AMap.Icon({
                        size: new AMap.Size(10, 10),
                        image: "img/red.png"
                    }),
                    position : [p.x, p.y],
                    title: p.name,
                    zIndex: 208
                }))
            }
        },
        drawRect: function (items) {
            var pic = document.getElementById("pic");
            var c=document.getElementById("myCanvas")
            $('#myCanvas').attr('width', pic.width)
            $('#myCanvas').attr('height', pic.height)
            var ctx=c.getContext("2d")
            for (var i = 0; i < items.length; i++) {
                var widthRatio = pic.width / pic.naturalWidth
                var heightRatio = pic.height / pic.naturalHeight
                var polygon = items[i].polygon.split(',')
                ctx.rect(polygon[0] * widthRatio, polygon[1] * heightRatio, polygon[2] * widthRatio, polygon[3] * heightRatio);
                ctx.strokeStyle = 'red'
                ctx.lineWidth = 3
                ctx.stroke()
            }
        },
        clearRect: function () {
            var pic = document.getElementById("pic");
            var c=document.getElementById("myCanvas")
            var ctx=c.getContext("2d")
            ctx.clearRect(0, 0, pic.width, pic.height)
        }
    },
    watch: {
        'curPicIndex': function (val, oldVal) {
            if (val > 0 && val <= this.request.pics.length) {
                this.curPic = this.request.pics[val - 1]
                this.map.setCenter([this.curPic.x, this.curPic.y])
                this.marker.setPosition([this.curPic.x, this.curPic.y])
                this.clearRect()

                if (this.infoWindow) {
                    this.infoWindow.close()
                }

                var ocrResult = this.ocr[this.curPic.id]
                if (ocrResult) {
                    for (var i = 0; i < ocrResult.length; i++) {
                        this.showInfoWindow(ocrResult[i])
                    }
                    this.drawRect(ocrResult)
                }
            }
        }
    }
})

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}





