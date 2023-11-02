if (!requestResponse.hasResponse()){
    return false;
}

long epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-01-01 00:00:00").getTime() / 1000;

if (epoch < requestResponse.time().toEpochSecond()){
	return true;
}

return false;
