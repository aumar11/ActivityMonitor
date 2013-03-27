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
query = strcat(query,num2str(19));%NEED TO CHANGE BACK TO ACTUAL ID!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
activity_type = getfield(curs.Data, 'activity_type');

%Filter the acceleromter data using a moving average filter
filterx = smooth(x, 15, 'moving');
filtery = smooth(y, 15, 'moving');
filterz = smooth(z, 15, 'moving');
xth = -5;
yth = -5;
zth = 4;

%Set our starting state
active =false;
if((filterx(1)<xth && filtery(1)>yth)||(filterx(1)<xth&& filterz(i) >zth)...
        || (filterz(1)>zth&& filtery >yth))
    active = false;
    strcat('Sedentary at ',timestamp(1))
else
    active = true
    strcat('Active at ',timestamp(1))
    
end

for i=2:length(filterx),
    if(active)%If we are active check for when we are change to sedentary
        if(filterx(i) < xth && filtery(i)> yth &&filterz(i)>zth)
            active = false;
            strcat('Sedentary at ',timestamp(i))
        end
    else %otherwise check for when we are becoming active
        if(filterx(i) > xth && filtery(i)< yth &&filterz(i)<zth)
            active = true;
            strcat('Active at ',timestamp(i))
        end
        
    end
end


%---------------------SET UP PLOT -----------------------------------------

%centimeters units
X = 21.0;                  %A5 paper size
Y = 14.8;                  %A5 paper size
xMargin = 1;               %left/right margins from page borders
yMargin = 1;               %bottom/top margins from page borders
xSize = X - xMargin;     %figure size on paper (widht & hieght)
ySize = Y - yMargin;     %figure size on paper (widht & hieght)

temp = strrep(activity_type(1), '-', '');
filename = strcat(temp, num2str(19));
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
title('Three Axis Accelerometer Data against Time');
xlabel('TimeStamp');
ylabel('Acceleration ms^-2');

%# figure size printed on paper
set(gcf, 'PaperUnits','centimeters');
set(gcf, 'PaperSize',[X Y]);
set(gcf, 'PaperPosition',[xMargin yMargin xSize ySize]);
set(gcf, 'PaperOrientation','portrait');
saveas(gcf, fn); %Save figure

figure('Menubar','none','Position',[1 scrsz(4)/2 scrsz(3)/2 scrsz(4)/2]);
plot(time,filterx,'x',time,filtery,'x',time,filterz,'x',time,-8,'-',...
    time, 4,'-',time,-2,'-');

set(gca,'XTick', time(1:20:end),'XTickLabel', timelabel,...
    'Units','normalized');

xticklabel_rotate([],45,[]);
hleg = legend('x axis','y axis','z axis',...
    'Location','NorthEastOutside');
% Make the text of the legend italic and color it brown
set(hleg,'FontAngle','italic','TextColor',[.3,.2,.1]);
title('Filtered Three Axis Accelerometer Data against Time');
xlabel('TimeStamp');
ylabel('Acceleration ms^-2');

%# figure size printed on paper
set(gcf, 'PaperUnits','centimeters');
set(gcf, 'PaperSize',[X Y]);
set(gcf, 'PaperPosition',[xMargin yMargin xSize ySize]);
set(gcf, 'PaperOrientation','portrait');
saveas(gcf,ffn ); %Save figure