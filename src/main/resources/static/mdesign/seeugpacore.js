function loadData() {
    var name = getCookie("username");
    var pass = getCookie("password");
    $.ajax({
        url: '/queryscorebyitem', // 跳转到 action
        data: {
            username: name,
            password: pass
        },
        type: 'post',
        dataType: 'json',
        success: function (data) {
            if (data.status == 1000) {
                initialPageData(data.data);
            } else {
                alert("请确认帐号密码再试");
            }
        },
        error: function () {
            alert("请确认帐号密码再重新尝试");
        }
    });
}

function setCookie(c_name, value, expiredays) {
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + expiredays);
    document.cookie = c_name + "=" + escape(value) +
        ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
}

function getCookie(c_name) {
    if (document.cookie.length > 0) {
        var c_start = document.cookie.indexOf(c_name + "=");
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1;
            var c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1) c_end = document.cookie.length;
            return unescape(document.cookie.substring(c_start, c_end));
        }
    }
    return "";
}

function initialPageData(data) {
    var info = data.info;
    var ing = data.ing;
    var ed = data.ed;
    var termNums = data.ed.length;
    initPersonalInfo(info);
    var zaixiudu = formTermZaiXiu(ing);
    var yixiudu = formTermYiXiuDu(ed);
    // document.getElementById("course__container").innerHTML = "" + zaixiudu + yixiudu;
    $("#coursecontainer").html("" + zaixiudu + yixiudu);
    //  计算总共修读的学分
    calInitialXffStatic(termNums);
    //  计算 GPA 分数
    for (var i = 0; i < termNums; i++) {
        calItemScore(i);
    }
    calTotalScore(termNums);

}
/**
 * 个人信息
 */
function initPersonalInfo(info) {
    document.getElementById("nickname").innerHTML = info.name;
    document.getElementById("sid").innerHTML = info.sid;
}


/*
 * 在修读课程
 */
function formTermZaiXiu(ingi) {
    var text = "<div class=\"row\"><div class=\"list-group list-group--block tasks-lists\">" +
        "<div class=\"list-group__header text-left\">" +
        "在修读课程" +
        "</div>";
    for (var i = 0; i < ingi.length; i++) {
        var item = formListItemZaiXiu(ingi[i]);
        text += item;
    }
    text += "</div></div>";
    return text;
}

function formListItemZaiXiu(data) {
    var id = "zaixiudu_" + data.id;
    var varchar = data.courseid.toString().charAt(0);
    var color = getColor(varchar);
    var item = "<div class=\"list-group-item\"><div class=\"checkbox checkbox--char\"><label><input id=\"checkbox_" +
        id + "\" type=\"checkbox\"><span class=\"checkbox__helper\">" +
        "<i class=\"" + color + "\">" + varchar + "</i></span><span class=\"tasks-list__info\">" +
        data.courseid + "  " + data.coursename +
        " <small class=\"text-muted\"><div class=\"listdetail\">" +
        "<item style=\"min-width:70px;\"><l>学分</l><xff id=\"xff_" + id + "\">" + data.xuefen + "</xff></item>" +
        //		"<item style=\"min-width:70px;\"><l>学时</l>" + data.period + "</item>" +
        //		"<item style=\"min-width:150px;\"><l>课程性质</l>" + data.xingzhi + "</item>" +
        "<item style=\"min-width:80px;\"><l>课程属性</l>" + data.shuxing + "</item>" +
        //		"<item style=\"min-width:80px;\"><l>考核方式</l>" + data.kaohe + "</item>" +
        //		"<item style=\"min-width:80px;\"><l>分数</l><cjj id=\"cjj_" + id + "\">" + data.score + "</cjj></item>" +
        //		"<item><l>评价</l>" + data.comment + "</item>" +
        "</div></small></span></label></div></div>";
    // document.write(item);
    return item;
}

/**
 * 已修读的成绩
 */
function formTermYiXiuDu(ed) {
    var content = "";
    for (var i = 0; i < ed.length; i++) {
        var term = formTerm(ed[i], i, ed.length);
        content += term;
    }
    return content;
}

/*
 * 创建每学期所有的记录
 */
function formTerm(edi, index, total) {
    var term = edi.term;
    var data = edi.data;
    var text = "<div class=\"row\"><div class=\"list-group list-group--block tasks-lists\">" +
        "<div class=\"list-group__header text-left\">" +
        term +
        "</div>";
    text += "<input id='term_" + index + "' type='hidden' value='" + data.length + "'></input>";
    for (var i = 0; i < data.length; i++) {
        var item = formListItem(data[i], index, total);
        text += item;
    }
    text += "<div class=\"list-group__header text-right\" style=\"border-bottom:none;float: right;\">" +
        "GPA:&nbsp;<seeuvalue id=\"gpa_" + index + "\" style=\"color:#e0a333\" >0.0</seeuvalue>";
    text += "</div></div></div>";
    return text;
}

/**
 * 创建每学期里每一条成绩记录
 * @param {Object} data
 */
function formListItem(data, index, total) {
    var id = index + "_" + data.id;
    var varchar = data.courseid.toString().charAt(0);
    var color = getColor(varchar);
    var item = "<div class=\"list-group-item\"><div class=\"checkbox checkbox--char\"><label onclick=\"clickBox(" + total + "," + index + ");\"><input id=\"checkbox_" +
        id + "\" type=\"checkbox\"><span class=\"checkbox__helper\">" +
        "<i class=\"" + color + "\">" + varchar + "</i></span><span class=\"tasks-list__info\">" +
        data.courseid + "  " + data.coursename +
        " <small class=\"text-muted\"><div class=\"listdetail\">" +
        "<item style=\"min-width:70px;\"><l>学分</l><xff id=\"xff_" + id + "\">" + data.credit + "</xff></item>" +
        "<item style=\"min-width:70px;\"><l>学时</l>" + data.period + "</item>" +
        "<item style=\"min-width:150px;\"><l>课程性质</l>" + data.xingzhi + "</item>" +
        "<item style=\"min-width:80px;\"><l>课程属性</l>" + data.shuxing + "</item>" +
        "<item style=\"min-width:80px;\"><l>考核方式</l>" + data.kaohe + "</item>" +
        "<item style=\"min-width:80px;\"><l>分数</l><cjj id=\"cjj_" + id + "\">" + data.score + "</cjj></item>" +
        "<item><l>评价</l>" + data.comment + "</item>" +
        "</div></small></span></label></div></div>";
    // document.write(item);
    return item;
}

function getColor(varchar) {
    switch (varchar) {
        case "C":
            return "mdc-bg-blue-300";
        case "G":
            return "mdc-bg-amber-300";
        case "H":
            return "mdc-bg-purple-300";
        case "B":
            return "mdc-bg-green-300";
        case "F":
            return "mdc-bg-blue-grey-300";
        case "M":
            return "mdc-bg-pink-300";
        case "E":
            return "mdc-bg-teal-300";
        case "S":
            return "mdc-bg-cyan-300";
        case "X":
            return "mdc-bg-orange-300";
        case "P":
            return "mdc-bg-indigo-300";
        case "I":
            return "mdc-bg-yellow-300";
        default:
            return "mdc-bg-red-300";
    }
}

function clickBox(totaltermLength, which) {
    calTotalScore(totaltermLength);
    calItemScore(which);
}
/**
 * 计算总共修读的学分
 * @param {Object} totaltermLength
 */
function calInitialXffStatic(totaltermLength) {
    var sumxf_all = 0;
    var sumxf = 0;
    for (var index = 0; index < totaltermLength; index++) {
        var itemdex = document.getElementById("term_" + index);
        // var itemNum = $("#term_" + index).val();
        var itemNum = itemdex.value;
        // 进入某一个学期：
        for (var i = 1; i <= itemNum; i++) {
            var xff = document.getElementById("xff_" + index + "_" + i);
            var cjj = document.getElementById("cjj_" + index + "_" + i);

            if (isNum(cjj.innerHTML)) {
                var cj = parseFloat(cjj.innerHTML);
                if (cj < 60) {
                    // 不计入总学分（属于挂科）
                    var xf = parseFloat(xff.innerHTML);
                    sumxf_all += xf;
                    continue;
                }
            } else if (cjj.innerHTML.toString() != "通过") {
                // 不计入总学分（属于未通过）
                var xf = parseFloat(xff.innerHTML);
                sumxf_all += xf;
                continue;
            }
            var xf = parseFloat(xff.innerHTML);
            sumxf += xf;
            sumxf_all += xf;
        }
    }
    if (sumxf != 0) {
        document.getElementById("xff_total_static").innerHTML = sumxf.toFixed(2)+" / "+sumxf_all;
    }
}
/**
 * 计算被勾选的总 GPA
 * @param {Object} totaltermLength
 */
function calTotalScore(totaltermLength) {
    var sumxf = 0;
    var sumscore = 0;
    var sumgpa = 0;
    for (var index = 0; index < totaltermLength; index++) {
        var itemdex = document.getElementById("term_" + index);
        var itemNum = itemdex.value;
        // 进入某一个学期：
        for (var i = 1; i <= itemNum; i++) {
            // 判断是否选中
            if ($("#checkbox_" + index + "_" + i).prop("checked") == false) {
                var xff = document.getElementById("xff_" + index + "_" + i);
                var cjj = document.getElementById("cjj_" + index + "_" + i);
                if (isNum(cjj.innerHTML)) {
                    var xf = parseFloat(xff.innerHTML);
                    var cj = parseFloat(cjj.innerHTML);
                    // alert(typeof cj);
                    var gpa = calGPAPoint(cj);
                    sumxf += xf;
                    sumscore += xf * cj;
                    sumgpa += xf * gpa;
                }
            }
        }
    }
    // if (sumxf != 0) {
        // var finalscore = parseFloat(sumscore) / parseFloat(sumxf); // 期望分数（直接算平均分）
        var wujigpa = parseFloat(sumgpa) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（15级）
        if( isNaN(wujigpa) ){
            wujigpa = 0;
        }
        // var baifengpa = parseFloat(sumgpa2) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（14级）
        document.getElementById("xff_total").innerHTML = sumxf.toFixed(2);
        document.getElementById("gpa_total").innerHTML = wujigpa.toFixed(2);
        // document.getElementById("baifen_gpa_total").innerHTML = baifengpa.toFixed(2);
    // }
}
/**
 * 计算每学期被勾选的 GPA
 * @param {Object} index:第几个学期
 */
function calItemScore(index) {
    var sumxf = 0;
    var sumscore = 0;
    var sumgpa = 0;
    var itemdex = document.getElementById("term_" + index);
    var itemNum = itemdex.value;
    for (var i = 0; i <= itemNum; i++) {
        // 判断是否选中
        if ($("#checkbox_" + index + "_" + i).prop("checked") == false) {
            var xff = document.getElementById("xff_" + index + "_" + i);
            var cjj = document.getElementById("cjj_" + index + "_" + i);
            if (isNum(cjj.innerHTML)) {
                var xf = parseFloat(xff.innerHTML);
                var cj = parseFloat(cjj.innerHTML);
                // alert(typeof cj);
                var gpa = calGPAPoint(cj);
                sumxf += xf;
                sumscore += xf * cj;
                sumgpa += xf * gpa;
            }
        }

    }
    if (sumxf != 0) {
        // var finalscore = parseFloat(sumscore) / parseFloat(sumxf); // 期望分数（直接算平均分）
        var wujigpa = parseFloat(sumgpa) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（15级）
        // var baifengpa = parseFloat(sumgpa2) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（14级）
        document.getElementById("gpa_" + index).innerHTML = wujigpa.toFixed(2);
        // document.getElementById("baifen_gpa" + index).innerHTML = baifengpa.toFixed(2);

    }

}

function isNum(str) {
    if (parseFloat(str) == "" + str) {
        return true;
    } else return false;
}

// 五级制计分（绩点 by score）2015级
function calGPAPoint(score) {
    score = parseFloat(score);
    if (score >= 97) return 4.00;
    if (score >= 93) return 3.94;
    if (score >= 90) return 3.85;
    if (score >= 87) return 3.73;
    if (score >= 83) return 3.55;
    if (score >= 80) return 3.32;
    if (score >= 77) return 3.09;
    if (score >= 73) return 2.78;
    if (score >= 70) return 2.42;
    if (score >= 67) return 2.08;
    if (score >= 63) return 1.63;
    if (score >= 60) return 1.15;
    if (score >= 0) return 0;
    else return 0;
}