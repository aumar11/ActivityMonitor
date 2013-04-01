javaaddpath 'mysql-connector-java-5.1.24-bin.jar';
%database properties
dbname = 'samples';
dbusername = 'root';
dbpassword = '';
driver = 'com.mysql.jdbc.Driver';
dburl = 'jdbc:mysql://localhost/';
dbST = 'sample'; %name of sample table
dbAT = 'activity'; %name of activity table

%connect to the datbase
conn=database(dbname, dbusername, dbpassword, driver, dburl);
%query to find the id of the latest activity on the database
query = 'SELECT id FROM activity order by id desc limit 1';
curs = exec(conn, query);
setdbprefs('DataReturnFormat','structure');
curs = fetch(curs);
%id of the lasest activity sample
id = getfield(curs.Data,'id');

%get all samples with the above activity_id
query = 'SELECT * FROM sample WHERE activity_id = ';
query = strcat(query,num2str(id));%NEED TO CHANGE BACK TO ACTUAL ID!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
curs = exec(conn,query);
setdbprefs('DataReturnFormat','structure');
curs = fetch(curs);

%close database connection
close(conn)

%parse the cursor information
id = getfield(curs.Data,'id');
x = getfield(curs.Data,'x');
y = getfield(curs.Data,'y');
z = getfield(curs.Data,'z');
timestamp = getfield(curs.Data,'timestamp');
time = datenum( timestamp, 'yyyy-mm-dd HH:MM:SS.FFF');
timelabel = datestr(time(1:20:end), 'HH:MM:SS.FFF');
timexlabel = datestr(time(1),'dd-mm-yyyy HH:MM:SS.FFF');

numberofsamples = length(x)
lengthoftime = time(end)-time(1);
timediff = datestr(lengthoftime, 'SS.FFF')
temp = sprintf('%s',timediff);
et = str2num(temp);
samplerate = numberofsamples / et
temp = struct2dataset(curs.Data);
summary(temp)

%---------------------SET UP PLOT -----------------------------------------

%centimeters units
X = 21.0;                  %A5 paper size
Y = 14.8;                  %A5 paper size
xMargin = 1;               %left/right margins from page borders
yMargin = 1;               %bottom/top margins from page borders
xSize = X - xMargin;     %figure size on paper (widht & hieght)
ySize = Y - yMargin;     %figure size on paper (widht & hieght)


%get screen size
scrsz = get(0,'ScreenSize');
figure('Position',[1 scrsz(4)/2 scrsz(3)/2 scrsz(4)/2]);

plot(time,x,'x',time,y,'x',time,z,'x');
%datetick('x','HH:MM:SS');


set(gca,'XTick', time(1:20:end),'XTickLabel', timelabel,...
    'Units','normalized');

xticklabel_rotate([],45,[]);
hleg = legend('x axis','y axis','z axis',...
    'Location','NorthEastOutside');
% Make the text of the legend italic and color it brown
set(hleg,'FontAngle','italic','TextColor',[.3,.2,.1]);
%title('Three Axis Accelerometer Data against Time','FontSize',16);
title('Female(20 To 30) Stand To Sit(1))','FontSize',14);
xlabel(sprintf('Timestamp starting %s', timexlabel),'FontSize',12);
ylabel('Acceleration ms^-2','FontSize',12);

%# figure size printed on paper
set(gcf, 'PaperUnits','centimeters');
set(gcf, 'PaperSize',[X Y]);
set(gcf, 'PaperPosition',[xMargin yMargin xSize ySize]);
set(gcf, 'PaperOrientation','portrait');
