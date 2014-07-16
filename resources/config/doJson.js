/**
 * 将Json字符串格式化成Json对象
 * @param data
 * @returns
 */
function getJsonObject(data){
	var obj = null;
	if(data != null && data != ""){
		obj = eval('(' + data + ')');
	}
	return obj;
}

/**
 * 获取保存操作之后返回的数据里面的ID
 * @param data
 * @returns
 */
function getDoSaveResponseId(data){
	var json_obj = getJsonObject(data);
	if(json_obj != null){
		return json_obj.message;
	}
	return null;
}

/**
 * 获取保存操作之后返回的数据里面的ID
 * @param data
 * @returns
 */
function getDoSaveEnjoyResponseId(data){
	var json_obj = getJsonObject(data);
	if(json_obj != null && "1000" == json_obj.status){
		return ""+json_obj.data;
	}
	return null;
}
// test rebase
function doTheJsonGetCount(data){
	var result_str = "";
	if(data != null && data != ""){
		//var json_data = JSON.parse(data);
		var json_data = eval('(' + data + ')');
		if(json_data != null && json_data.status == "1000"){
			result_str = json_data.data.recordCount;
		}
	}
	return result_str;
}

function doTheJson(data){
	var array_Type = Java.type("java.lang.String[]");
	
	if(data != null && data != ""){
		//var json_data = JSON.parse(data);
		var json_data = eval('(' + data + ')');
		if(json_data != null && json_data.status == "1000"){
			var list = json_data.data.listPageObject;
			if(list != null){
				var result_str = new array_Type(list.length);
				for(var i = 0; i < list.length;i++){
					var item = list[i];
					result_str[i]= item.url;
				}
				return result_str;
			}
		}
		
	}
	return null;
}