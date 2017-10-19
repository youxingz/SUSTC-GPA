startwork();

function startwork() {
	var name = getCookie("username");
	var pass = getCookie("password");
	$.ajax({
		url: '/queryscorebyitem', // 跳转到 action
		data:{
			username:name,
			password:pass
		},
		type: 'post',
		dataType: 'json',
		success: function(data) {
			if(data.status == 1000) {
				afterpost(data.data);
			} else {
				alert("请确认帐号密码再试");
			}
		},
		error: function() {
			alert("请确认帐号密码再重新尝试");
		}
	});

	//	var datas = "{ \"data\": {\"data\": [[\"CH101\",\"化学原理\",\"4\",\"通识必修课\",\"必修\",\"97\",\"&nbsp;\"]]}}";
	//	var json = JSON.parse(datas);
	//	loadscore(json.data);
	//	calTotalScore();
}

function afterpost(datas) {
	processING(datas.ing);
	processED(datas.ed);
	// var json = JSON.parse(datas);
	// alert(datas);
	var json = (datas);

	// 去掉 loading 进度条
	var loading = document.getElementById("loading");
	loading.innerHTML = "";
	loading.className = "";
	
	calTotalScore();
}

function setCookie(c_name, value, expiredays) {
	var exdate = new Date()
	exdate.setDate(exdate.getDate() + expiredays)
	document.cookie = c_name + "=" + escape(value) +
		((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}

function getCookie(c_name) {
	if(document.cookie.length > 0) {
		c_start = document.cookie.indexOf(c_name + "=")
		if(c_start != -1) {
			c_start = c_start + c_name.length + 1
			c_end = document.cookie.indexOf(";", c_start)
			if(c_end == -1) c_end = document.cookie.length
			return unescape(document.cookie.substring(c_start, c_end))
		}
	}
	return ""
}

function processING(data) {
	var tbody = handleTBodyWithCreatedTable("在修读课程", 999999);

	var th = document.createElement("th");
	var titles = "[\"课程编号\",\"课程名称\",\"学分\",\"课程性质\"]";

	titles = JSON.parse(titles);
	var th = document.createElement("tr");
	var content = ""; // 第一个不用加空位
	for(var j = 0; j < titles.length; j++) {
		content += "<td>" + titles[j] + "</td>";
	}
	th.innerHTML = content;

	tbody.appendChild(th);
	for(var i = 0; i < data.length; i++) {
		var tr = document.createElement("tr");
		tr.setAttribute("id", "ingline" + i);
		var content = ""; // no color
		content += "<td>" + data[i].courseid + "</td>";
		content += "<td>" + data[i].coursename + "</td>";
		content += "<td>" + data[i].xuefen + "</td>";
		content += "<td>" + data[i].shuxing + "</td>";
		tr.innerHTML = content;
		tbody.appendChild(tr);
	}
}

function processED(data) {
	for(var i = 0; i < data.length; i++) {
		processEDItem(data[i], i);
		calItemScore(i); // cal the score auto
	}
	calTotalScore();
}
/**
 * @param {Object} data
 * @param {Object} index is provided for the id
 */
function processEDItem(data, index) {
	var termtitle = data.term;
	var tbody = handleTBodyWithCreatedTable(termtitle, index);

	var th = document.createElement("th");
	titles = "[\"课程编号\",\"课程名称\",\"学分\",\"学时\",\"课程性质\",\"课程属性\",\"考核方式\",\"分数\",\"评价\"]";
	titles = JSON.parse(titles);
	var th = document.createElement("tr");
	var content = "<td></td>"; // 第一个为空位
	for(var j = 0; j < titles.length; j++) {
		content += "<td>" + titles[j] + "</td>";
	}
	th.innerHTML = content;
	tbody.appendChild(th);

	var ddata = data.data;
	for(var i = 0; i < ddata.length; i++) {
		var tr = document.createElement("tr");
		tr.setAttribute("id", "line" + index + "_" + i);
		var content = "" + makecolor(index, i); // color
		content += "<td>" + ddata[i].courseid + "</td>";
		content += "<td>" + ddata[i].coursename + "</td>";
		content += "<td id=\"xff" + index + "_" + i + "\">" + ddata[i].credit + "</td>"; // 学分
		content += "<td>" + ddata[i].period + "</td>";
		content += "<td>" + ddata[i].xingzhi + "</td>";
		content += "<td>" + ddata[i].shuxing + "</td>";
		content += "<td>" + ddata[i].kaohe + "</td>";
		content += "<td id=\"cjj" + index + "_" + i + "\">" + ddata[i].score + "</td>"; //成绩
		//		content += "<td>" + calGPA(ddata[i].score) + "</td>"; //GPA评价（数据来自本地计算）
		content += "<td>" + ddata[i].comment + "</td>"; // GPA评价（数据来自服务器）
		tr.innerHTML = content;
		tbody.appendChild(tr);
	}
	document.getElementById("hidden" + index).value = ddata.length;
}

/**
 * 创建table并返回tbody操作
 */
function handleTBodyWithCreatedTable(title, index) {

	var div = document.createElement("div");
	div.setAttribute("class", "seeubody");
	//title
	var h3 = document.createElement("h3");
	h3.innerHTML = title;
	div.appendChild(h3);
	//table
	var table = document.createElement("table");
	table.setAttribute("class", "table");
	var tbody = document.createElement("tbody");
	table.appendChild(tbody);
	div.appendChild(table);

	if(index != 999999) { // not the ING tab
		var input = document.createElement("input");
		input.setAttribute("id", "hidden" + index);
		input.setAttribute("value", "0");
		input.setAttribute("type", "hidden");
		input.setAttribute("class", "seeuhidden");
		div.appendChild(input);

		var divgpa = document.createElement("div");
		divgpa.setAttribute("style", "margin-top: 20px;padding-bottom: 10px;");
		// divgpa.innerHTML = "<div id=\"wuji_gpa" + index + "\" style=\"float: right;color: #e0a333;font-size: 20px;margin-top: -5px;margin-right: 80px;\">0.00</div><div style=\"float: right;margin-right: 10px;\">五级制：</div><div id=\"baifen_gpa" + index + "\" style=\"float: right;color: #6278a0;font-size: 20px;margin-top: -5px;margin-right: 40px;\">0.00</div><div style=\"float: right;margin-right: 10px;\">百分制：</div>";
		divgpa.innerHTML = "<div id=\"wuji_gpa" + index + "\" style=\"float: right;color: #e0a333;font-size: 20px;margin-top: -5px;margin-right: 80px;\">0.00</div><div style=\"float: right;margin-right: 10px;\">GPA ：</div>";

		div.appendChild(divgpa);
	}
	document.getElementById("seeu").appendChild(div);
	return tbody;
}

function makecolor(index, which) {
	// 根据circle判断是否激活
	var color = randomColor();
	return "<td><div class=\"circle " + color + "\" id=\"circle" + index + "_" + which + "\" onclick=\"clickCircle(" + index + "," + which + ");\"></div></td>";
}

function randomColor() {
	var rdom = Math.floor(5 * Math.random());
	switch(rdom) {
		case 0:
			return "orange-bg";
		case 1:
			return "yellow-bg";
		case 2:
			return "blue-bg";
		case 3:
			return "pink-bg";
		case 4:
			return "green-bg";
		default:
			return "pink-bg";
	}
}
// 五级制计分（GPA等级 by score）2015级
function calGPA(score) {
	score = parseFloat(score);
	if(score >= 97) return "A+";
	if(score >= 93) return "A";
	if(score >= 90) return "A-";
	if(score >= 87) return "B+";
	if(score >= 83) return "B";
	if(score >= 80) return "B-";
	if(score >= 77) return "C+";
	if(score >= 73) return "C";
	if(score >= 70) return "C-";
	if(score >= 67) return "D+";
	if(score >= 63) return "D";
	if(score >= 60) return "D-";
	if(score >= 0) return "F";
	else return "";
}
// 五级制计分（绩点 by score）2015级
function calGPAPoint(score) {
	score = parseFloat(score);
	if(score >= 97) return 4.00;
	if(score >= 93) return 3.94;
	if(score >= 90) return 3.85;
	if(score >= 87) return 3.73;
	if(score >= 83) return 3.55;
	if(score >= 80) return 3.32;
	if(score >= 77) return 3.09;
	if(score >= 73) return 2.78;
	if(score >= 70) return 2.42;
	if(score >= 67) return 2.08;
	if(score >= 63) return 1.63;
	if(score >= 60) return 1.15;
	if(score >= 0) return 0;
	else return 0;
}
// 五级制计分（GPA等级 by gpa value）2015级
function calGPAABC(gpa) {
	score = parseFloat(gpa);
	if(score >= 4.00) return "A+";
	if(score >= 3.94) return "A";
	if(score >= 3.85) return "A-";
	if(score >= 3.73) return "B+";
	if(score >= 3.55) return "B";
	if(score >= 3.32) return "B-";
	if(score >= 3.09) return "C+";
	if(score >= 2.78) return "C";
	if(score >= 2.42) return "C-";
	if(score >= 2.08) return "D+";
	if(score >= 1.63) return "D";
	if(score >= 1.15) return "D-";
	if(score >= 0) return "F";
	else return "";
}

// listener:
function clickCircle(index, which) {
	var circle = document.getElementById("circle" + index + "_" + which);
	//	var lineid = "line" + index + "_" + which;
	//	var line = document.getElementById(lineid);
	if(circle.className.toString().indexOf("white-bg") == -1) {
		// 如果打开
		circle.className = "circle white-bg";
		calItemScore(index);
		calTotalScore();
		//					alert(circle.className.toString());
	} else {
		// 如果已经被关了
		circle.className = "circle " + randomColor();
		calItemScore(index);
		calTotalScore();
	}
}

function calTotalScore() {
	var hiddens = document.getElementsByClassName("seeuhidden");
	var sumxf = 0;
	var sumscore = 0;
	var sumgpa = 0;
	var sumgpa2 = 0;
	for(var index = 0; index < hiddens.length; index++) {
		for(var i = 0; i < hiddens[index].value; i++) {
			// 判断是否选中
			var circle = document.getElementById("circle" + index + "_" + i);
			if(circle.className.toString().indexOf("white-bg") == -1) {
				var xff = document.getElementById("xff" + index + "_" + i);
				var cjj = document.getElementById("cjj" + index + "_" + i);
				if(isNum(cjj.innerHTML)) {
					var xf = parseFloat(xff.innerHTML);
					var cj = parseFloat(cjj.innerHTML);
					// alert(typeof cj);
					var gpa = calGPAPoint(cj);
					var gpa2 = calBaiFenGPA(cj);
					sumxf += xf;
					sumscore += xf * cj;
					sumgpa += xf * gpa;
					sumgpa2 += xf * gpa2;
				}
			}
		}
	}
	if(sumxf != 0) {
		// var finalscore = parseFloat(sumscore) / parseFloat(sumxf); // 期望分数（直接算平均分）
		var wujigpa = parseFloat(sumgpa) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（15级）
		// var baifengpa = parseFloat(sumgpa2) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（14级）
		document.getElementById("wuji_gpa_total").innerHTML = wujigpa.toFixed(2);
		// document.getElementById("baifen_gpa_total").innerHTML = baifengpa.toFixed(2);

	}
}

function calItemScore(index) {
	var totalnum = document.getElementById("hidden" + index).value;
	var sumxf = 0;
	var sumscore = 0;
	var sumgpa = 0;
	var sumgpa2 = 0;
	for(var i = 0; i < totalnum; i++) {
		// 判断是否选中
		var circle = document.getElementById("circle" + index + "_" + i);
		if(circle.className.toString().indexOf("white-bg") == -1) {
			var xff = document.getElementById("xff" + index + "_" + i);
			var cjj = document.getElementById("cjj" + index + "_" + i);
			if(isNum(cjj.innerHTML)) {
				var xf = parseFloat(xff.innerHTML);
				var cj = parseFloat(cjj.innerHTML);
				// alert(typeof cj);
				var gpa = calGPAPoint(cj);
				var gpa2 = calBaiFenGPA(cj);
				sumxf += xf;
				sumscore += xf * cj;
				sumgpa += xf * gpa;
				sumgpa2 += xf * gpa2;
			}
		}
	}
	if(sumxf != 0) {
		// var finalscore = parseFloat(sumscore) / parseFloat(sumxf); // 期望分数（直接算平均分）
		var wujigpa = parseFloat(sumgpa) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（15级）
		// var baifengpa = parseFloat(sumgpa2) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（14级）
		document.getElementById("wuji_gpa" + index).innerHTML = wujigpa.toFixed(2);
		// document.getElementById("baifen_gpa" + index).innerHTML = baifengpa.toFixed(2);

	}

}

function calTotalScoreXX() {
	var tbody = document.getElementById("seeu");
	var totalnum = document.getElementById("totallines").value;
	var sumxf = 0;
	var sumscore = 0;
	var sumgpa = 0;
	var sumgpa2 = 0;
	for(var i = 0; i < totalnum; i++) {
		// 判断是否选中
		var circle = document.getElementById("circle" + i);
		if(circle.className.toString().indexOf("white-bg") == -1) {
			var xff = document.getElementById("xff" + i);
			var cjj = document.getElementById("cjj" + i);
			if(isNum(cjj.innerHTML)) {
				var xf = parseFloat(xff.innerHTML);
				var cj = parseFloat(cjj.innerHTML);
				// alert(typeof cj);
				var gpa = calGPAPoint(cj);
				var gpa2 = calBaiFenGPA(cj);
				sumxf += xf;
				sumscore += xf * cj;
				sumgpa += xf * gpa;
				sumgpa2 += xf * gpa2;
			}
		}
	}
	if(sumxf != 0) {

		// var finalscore = parseFloat(sumscore) / parseFloat(sumxf); // 期望分数（直接算平均分）
		var wujigpa = parseFloat(sumgpa) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（15级）
		// var baifengpa = parseFloat(sumgpa2) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（14级）
		document.getElementById("wuji_gpa").innerHTML = wujigpa.toFixed(2);
		// document.getElementById("baifen_gpa").innerHTML = baifengpa.toFixed(2);

		//		var abcdiv = document.getElementById("finalabc");
		//		var scorediv = document.getElementById("finalscore");
		//		var gpadiv = document.getElementById("finalgpa");
		//
		//         var finalgpa = parseFloat(sumgpa) / parseFloat(sumxf); // 期望绩点（先算GPA再平均）（15级）
		//         var finalgpaabc = calGPAABC(finalgpa); // 期望绩点对应的等级
		//
		//         var finalscore = parseFloat(sumscore) / parseFloat(sumxf); // 期望分数（直接算平均分 15级）
		//         var finalscoreabc = calGPA(finalscore); // 期望分数对应的ABCDF等级
		//         var finalscoregpa = calGPAPoint(finalscore); // 期望分数对应的绩点
		// //		abcdiv.innerHTML = finalscoreabc;
		// //		scorediv.innerHTML = finalscore.toFixed(2);
		//
		//         var standscore = finalscore; // 百分制的分数算法与普通的五级制一致
		//         // var standgpa = (finalscore * 4 / 100.00); // 百分比换算制 GPA（14级）
		//
		//         // var standgpa = calBaiFenGPA(finalscore); // 百分比换算制 GPA（14级）
		//         var standgpa = parseFloat(sumgpa2)/parseFloat(sumxf); // 期望绩点（先算GPA再平均）（14级）
		//         var standabc = calGPAABC(standgpa); // 百分比gpa对应的等级
		// //		gpadiv.innerHTML = standgpa;
		//
		//         document.getElementById("wj1_score").innerHTML = finalscore.toFixed(2);
		//         document.getElementById("wj1_gpa").innerHTML = finalscoregpa.toFixed(2);
		//         // document.getElementById("wj1_abc").innerHTML = finalscoreabc;
		//
		//         document.getElementById("wj2_gpa").innerHTML = finalgpa.toFixed(2);
		//         document.getElementById("wj2_abc").innerHTML = finalgpaabc;
		//
		//         document.getElementById("bf_score").innerHTML = standscore.toFixed(2);
		//         document.getElementById("bf_gpa").innerHTML = standgpa.toFixed(2);
		// document.getElementById("bf_abc").innerHTML = standabc;
	}
}

function isNum(str) {
	if(parseFloat(str) == "" + str) {
		return true;
	} else return false;
}

function calBaiFenGPA(score) {
	if(score >= 100) return 4;
	if(score >= 99) return 4;
	if(score >= 98) return 3.99;
	if(score >= 97) return 3.98;
	if(score >= 96) return 3.97;
	if(score >= 95) return 3.95;
	if(score >= 94) return 3.93;
	if(score >= 93) return 3.91;
	if(score >= 92) return 3.88;
	if(score >= 91) return 3.85;
	if(score >= 90) return 3.81;
	if(score >= 89) return 3.77;
	if(score >= 88) return 3.73;
	if(score >= 87) return 3.68;
	if(score >= 86) return 3.63;
	if(score >= 85) return 3.58;
	if(score >= 84) return 3.52;
	if(score >= 83) return 3.45;
	if(score >= 82) return 3.39;
	if(score >= 81) return 3.32;
	if(score >= 80) return 3.25;
	if(score >= 79) return 3.17;
	if(score >= 78) return 3.09;
	if(score >= 77) return 3.01;
	if(score >= 76) return 2.92;
	if(score >= 75) return 2.83;
	if(score >= 74) return 2.73;
	if(score >= 73) return 2.63;
	if(score >= 72) return 2.53;
	if(score >= 71) return 2.42;
	if(score >= 70) return 2.31;
	if(score >= 69) return 2.2;
	if(score >= 68) return 2.08;
	if(score >= 67) return 1.96;
	if(score >= 66) return 1.83;
	if(score >= 65) return 1.7;
	if(score >= 64) return 1.57;
	if(score >= 63) return 1.43;
	if(score >= 62) return 1.29;
	if(score >= 61) return 1.15;
	if(score >= 60) return 1;
	if(score >= 0) return 0;
	else return 0;
}