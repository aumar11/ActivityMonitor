javaaddpath 'mysql-connector-java-5.1.24-bin.jar';
conn=database('samples', 'root', '', 'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost/');
curs = exec(conn,...
    'SELECT * FROM sample where activity_type = ''s-w-s''and activity_id = 19');
setdbprefs('DataReturnFormat','structure');
curs = fetch(curs);
id = getfield(curs.Data,'id');
x = getfield(curs.Data,'x');
y = getfield(curs.Data,'y');
z = getfield(curs.Data,'z');
timestamp = getfield(curs.Data,'timestamp');
close(conn);
xth = -5;
yth = -5;
zth = 4;

filterx = smooth(x, 15, 'moving');
filtery = smooth(y, 15, 'moving');
filterz = smooth(z, 15, 'moving');
length(filterx)
length(filtery)
length(filterz)
ticknum = length(id);
ticknum = round(ticknum/20);
time = datenum( timestamp, 'yyyy-mm-dd HH:MM:SS.FFF');
active = true;

for i=1:length(filterx),
    if(active)
        if(filterx(i) < xth && filtery(i)> yth &&filterz(i)>zth)
            active = false;
            strcat('Sedentary at ',timestamp(i))
        end
    else
        if(filterx(i) > xth && filtery(i)< yth &&filterz(i)<zth)
            active = true;
            strcat('Active at ',timestamp(i))
        end
      
    end
end

%time2 = datestr(timestamp,'HH:MM:SS.FFF');

%# centimeters units
X = 21.0;                  %# A3 paper size
Y = 14.8;                  %# A3 paper size
xMargin = 1;               %# left/right margins from page borders
yMargin = 1;               %# bottom/top margins from page borders
xSize = X - xMargin;     %# figure size on paper (widht & hieght)
ySize = Y - yMargin;     %# figure size on paper (widht & hieght)

%# create figure/axis
fig = figure('Menubar','none');

plot(time,filterx,'x',time,filtery,'x',time,filterz,'x');
datetick('x','HH:MM:SS');
set(gca,'XTick', time(1:ticknum:end),'XTickLabel', timestamp(1:ticknum:end),...
    'Units','normalized');
%set(gca,'XTickLabel', timestamp(1:20:end));
xticklabel_rotate([],45,[]);
hleg = legend('x axis','y axis','z axis',...
    'Location','NorthEastOutside');
% Make the text of the legend italic and color it brown
set(hleg,'FontAngle','italic','TextColor',[.3,.2,.1]);
title('Three Axis Accelerometer Data against Time');
xlabel('TimeStamp');
ylabel('Acceleration ms^-2');

%# figure size on screen (50% scaled, but same aspect ratio)
set(gcf, 'Units','centimeters', 'Position',[15 15 xSize ySize])

%# figure size printed on paper
set(gcf, 'PaperUnits','centimeters');
set(gcf, 'PaperSize',[X Y]);
set(gcf, 'PaperPosition',[xMargin yMargin xSize ySize]);
set(gcf, 'PaperOrientation','portrait');

filename = 'filter.pdf';
%# export to PDF and open file
%print -dpdf -r0 saveas
%print(fig, '-dpdf', saveas) ;
saveas(gcf, filename); %Save figure

