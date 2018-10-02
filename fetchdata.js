const fs = require('fs')

let data = fs.readFileSync("./ScheduleGenerator.html","utf8")
const setUpregex = /currentSectionData[\s\S]*degreePlanCourses?/gm
const regex = /("course":"\d\d\d")|("name":".*?")|("subject":".*?")|("title":".*?")|("days":".*?")|("startTime":.*?")|(endTime".*?")|(startDate":"..........)|(endDate":"..........)|(buildingCode":"...)|(room":"...")/gm


data = setUpregex.exec(data)
data = data[0];
const dataA= data.match(regex);
for (i = 0; i <dataA.length; i++){
	dataA[i]=dataA[i].replace(/[",']/g, "")
	dataA[i] = dataA[i].substring(dataA[i].indexOf(':')+1)
}



class calenderObject
{
	constructor(name, days, startTime, endTime, startDate, endDate, building, room, courseNum, subject, classTitle){
	this.classTitle=classTitle
	this.name=name
	this.days = days
	this.startTime = startTime
	this.endTime = endTime
	this.startDate = startDate
	this.endDate = endDate
	this.building = building
	this.room = room
	this.subject = subject
	this.courseNum = courseNum
	
	}
	
	
}


function objFactory(array)
{
	const num = array.length/11
	let objArray = []
	for(i = 0; i < num; ++i){
		objArray[i] = new calenderObject(array[0],array[1],array[2],array[3],array[4],array[5],array[6],array[7],array[8],array[9],array[10])
		
		for(j = 0; j <11; ++j)
			array.shift();
		
	}
	return objArray;
}

let classArray = objFactory(dataA);
console.log(JSON.stringify(classArray,null,"\t"))