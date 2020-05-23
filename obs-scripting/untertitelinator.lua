local obs = obslua
local textFileLine1, textFileLine2, interval, debug, line1Name, line2Name -- OBS settings
local activeId = 0 -- active timer id
local current = {} -- current values to compare with text file


-- called when an update to the text file is detected
local function update(line, sourceName)
	local source = obs.obs_get_source_by_name(sourceName)
	if source ~= nil then
		local settings = obs.obs_data_create()
		obs.obs_data_set_string(settings, "text", line)
		obs.obs_source_update(source, settings)
		obs.obs_data_release(settings)
		obs.obs_source_release(source)
	end
end


local function checkFile(id)
	-- if the script has reloaded then stop any old timers
	if id < activeId then
		obs.remove_current_callback()
		return
	end

	if debug then obs.script_log(obs.LOG_INFO, string.format("(%d) Checking text file %s...(%d)", id, textFileLine1, interval)) end
	
	local linesTextFile1 = ""
	for line in io.lines(textFileLine1) do 
	linesTextFile1 = linesTextFile1 .. line .. "\n"
	end
	update(linesTextFile1, line1Name)
	
	if debug then obs.script_log(obs.LOG_INFO, string.format("(%d) Checking text file %s...(%d)", id, textFileLine2, interval)) end
	
	local linesTextFile2 = ""
	for line in io.lines(textFileLine2) do 
	linesTextFile2 = linesTextFile2 .. line .. "\n"
	end
	update(linesTextFile2, line2Name)
	
end


local function init()
	-- increase the timer id - old timers will be cancelled
	activeId = activeId + 1

	-- only proceed if there is a text file selected
	if not textFileLine1 then return nil end
	if not textFileLine2 then return nil end
	
	-- start the timer to check the text file
	local id = activeId
	obs.timer_add(function() checkFile(id) end, interval)
	obs.script_log(obs.LOG_INFO, string.format("Text monitor started"))
end


----------------------------------------------------------


-- called on startup
function script_load(settings)
end


-- called on unload
function script_unload()
end


-- called when settings changed
function script_update(settings)
	textFileLine1 = obs.obs_data_get_string(settings, "textFileLine1")
	textFileLine2 = obs.obs_data_get_string(settings, "textFileLine2")
	line1Name = obs.obs_data_get_string(settings, "line1Name")
	line2Name = obs.obs_data_get_string(settings, "line2Name")
	interval = obs.obs_data_get_int(settings, "interval")
	debug = obs.obs_data_get_bool(settings, "debug")
	init()
end


-- return description shown to user
function script_description()
	return "Monitor a text file for changes"
end


-- define properties that user can change
function script_properties()
	local props = obs.obs_properties_create()
	obs.obs_properties_add_path(props, "textFileLine1", "Textdatei des Textfelds der ersten Liedzeile", obs.OBS_PATH_FILE, "", nil)
	obs.obs_properties_add_text(props, "line1Name", "Source-Name der ersten Liedzeile", obs.OBS_TEXT_DEFAULT)
	obs.obs_properties_add_path(props, "textFileLine2", "Textdatei der zweiten Liedzeile", obs.OBS_PATH_FILE, "", nil)
	obs.obs_properties_add_text(props, "line2Name", "Source-Name des Textfelds der zweiten Liedzeile", obs.OBS_TEXT_DEFAULT)
	obs.obs_properties_add_int(props, "interval", "Aktualisierungs-Interval (ms)", 100, 20000, 500)
	obs.obs_properties_add_bool(props, "debug", "Debug")
	return props
end


-- set default values
function script_defaults(settings)
	obs.obs_data_set_default_string(settings, "textFileLine1", "")
	obs.obs_data_set_default_string(settings, "textFileLine2", "")
	obs.obs_data_set_default_string(settings, "line1Name", "Liedzeile 1")
	obs.obs_data_set_default_string(settings, "line1Name", "Liedzeile 2")
	obs.obs_data_set_default_int(settings, "interval", 100)
	obs.obs_data_set_default_bool(settings, "debug", false)
end


-- save additional data not set by user
function script_save(settings)
end