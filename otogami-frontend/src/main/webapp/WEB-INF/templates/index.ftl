<html>
<head>
<title>Otogami - Videogame search engine</title>
<body>
	<h2>Which videogame are you looking for?</h2>

	<fieldset style="margin-top: 20px">
		<legend>
			<strong>Introduce your search criteria</strong>
		</legend>
		<form name="videogame" action="search" method="post">
    		Platform: 		<select name="platform">
								<#list model.platformList as platform>
			  						<option value="${platform}" <#if platform == model.input.platform!>selected</#if>>${platform}</option>
								</#list> 
							</select>  <br/>
			Title: 			<input type="text" name="title" value=${model.input.title!}> <br /> 
			Minimum price: 	<input type:"text" name="price" value=${model.input.price!}><br />
			Availability: 	<select name="availability">
								<#list model.availabilityList as availability>
			  						<option value="${availability}" <#if availability == model.input.availability!>selected</#if>>${availability}</option>
								</#list> 
							</select>  <br/>
			<input type="submit" value="Go and find it!" />
		</form>
		<div id="errors">
			<font color="red"> <#if model.errorMessage??>${model.errorMessage}</#if>
			</font>
		</div>
	</fieldset>

	<table class="datatable">
		<tr>
			<th>Platform</th>
			<th>Title</th>
			<th>Availability</th>
			<th>Price</th>
			<th>Link to the game</th>
		</tr>

		<#list model.videogames as game>
		<tr>
			<#escape x as x?html>
			<td>${game.platform}</td>
			<td>${game.title}</td>
			<td>${game.availability!N/A}</td>
			<td><#if game.price??>${game.price?string.currency}<#else>N/A</#if></td>
			<td><a target="_blank" href="${game.url!'#noUrlFound'}">Let's
					go for it!</a></td> </#escape>
		</tr>
		</#list>
	</table>
</body>