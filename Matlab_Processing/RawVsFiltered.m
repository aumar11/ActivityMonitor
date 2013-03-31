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
activity_type = getfield(curs.Data, 'activity_type');

%Filter the acceleromter data using a moving average filter
filterx = smooth(x, 15, 'moving');
filtery = smooth(y, 15, 'moving');
filterz = smooth(z, 15, 'moving');
xth = -5;
yth = -5;
zth = 4;




%---------------------SET UP PLOT -----------------------------------------

%centimeters units
X = 21.0;                  %A5 paper size
Y = 14.8;                  %A5 paper size
xMargin = 1;               %left/right margins from page borders
yMargin = 1;               %bottom/top margins from page borders
xSize = X - xMargin;     %figure size on paper (widht & hieght)
ySize = Y - yMargin;     %figure size on paper (widht & hieght)

temp = strrep(activity_type(1), '-', '');
filename = strcat(temp, num2str(id));
filename = strcat(filename, '.pdf');
fn = filename{1}; % Oh god wat?! Char array's 'n' shit.
filterfilename = strcat('Filter', filename);
ffn = filterfilename{1};

%get screen size
scrsz = get(0,'ScreenSize');
figure('Menubar','none','Position',[1 scrsz(4)/2 scrsz(3)/2 scrsz(4)/2]);

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
title('SWS (Raw Data)','FontSize',14);
xlabel(sprintf('Timestamp starting %s', timexlabel),'FontSize',12);
ylabel('Acceleration ms^-2','FontSize',12);

%# figure size printed on paper
set(gcf, 'PaperUnits','centimeters');
set(gcf, 'PaperSize',[X Y]);
set(gcf, 'PaperPosition',[xMargin yMargin xSize ySize]);
set(gcf, 'PaperOrientation','portrait');
saveas(gcf, 'SWSRaw.pdf'); %Save figure


plot(time,filterx,'x',time,filtery,'x',time,filterz,'x');
%datetick('x','HH:MM:SS');


set(gca,'XTick', time(1:20:end),'XTickLabel', timelabel,...
    'Units','normalized');

xticklabel_rotate([],45,[]);
hleg = legend('x axis','y axis','z axis',...
    'Location','NorthEastOutside');
% Make the text of the legend italic and color it brown
set(hleg,'FontAngle','italic','TextColor',[.3,.2,.1]);
%title('Three Axis Accelerometer Data against Time','FontSize',16);
title('SWS (Filter Data)','FontSize',14);
xlabel(sprintf('Timestamp starting %s', timexlabel),'FontSize',12);
ylabel('Acceleration ms^-2','FontSize',12);

%# figure size printed on paper
set(gcf, 'PaperUnits','centimeters');
set(gcf, 'PaperSize',[X Y]);
set(gcf, 'PaperPosition',[xMargin yMargin xSize ySize]);
set(gcf, 'PaperOrientation','portrait');
saveas(gcf, 'SWSFiltered.pdf'); %Save figure
