%javaaddpath 'mysql-connector-java-5.1.24-bin.jar';
conn=database('samples', 'root', '', 'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost/');
query = exec(conn,'SELECT * FROM sample WHERE activity_id = 2');
setdbprefs('DataReturnFormat','structure');
query = fetch(query);
id = getfield(query.Data,'id');
x = getfield(query.Data,'x');
y = getfield(query.Data,'y');
z = getfield(query.Data,'z');
timestamp = getfield(query.Data,'timestamp');
close(conn);
time = datenum( timestamp, 'yyyy-mm-dd HH:MM:SS.FFF');
%# centimeters units
X = 21.0;                  %# A3 paper size
Y = 14.8;                  %# A3 paper size
xMargin = 1;               %# left/right margins from page borders
yMargin = 1;               %# bottom/top margins from page borders
xSize = X - 2*xMargin;     %# figure size on paper (widht & hieght)
ySize = Y - 2*yMargin;     %# figure size on paper (widht & hieght)

%# create figure/axis
figure('Menubar','none')

plot(time,x,'x',time,y,'x',time,z,'x');
datetick('x','dd-mm HH:MM:SS');
set(gca,'XTick', time(1:20:end),'XTickLabel', timestamp(1:20:end))%,...
%'Units','normalized', 'Position',[0 0 1 1]);
%set(gca,'XTickLabel', timestamp(1:20:end));
xticklabel_rotate([],45,[]);
hleg = legend('x axis','y axis','z axis',...
              'Location','NorthWest');
% Make the text of the legend italic and color it brown
set(hleg,'FontAngle','italic','TextColor',[.3,.2,.1]);
title('Three Axis Accelerometer Data against Time');
xlabel('TimeStamp');
ylabel('Acceleration ms^-2');

%# figure size on screen (50% scaled, but same aspect ratio)
set(gcf, 'Units','centimeters', 'Position',[15 15 xSize ySize])

%# figure size printed on paper
set(gcf, 'PaperUnits','centimeters')
set(gcf, 'PaperSize',[X Y])
set(gcf, 'PaperPosition',[xMargin yMargin xSize ySize])
set(gcf, 'PaperOrientation','portrait')

%# export to PDF and open file
print -dpdf -r0 out.pdf