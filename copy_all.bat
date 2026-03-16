@echo off
mkdir "c:\Users\prart\OneDrive\Desktop\BananaLeaf\branch-sales-backend\src\main\resources\static\assets" 2>nul
copy /Y "c:\Users\prart\OneDrive\Desktop\BananaLeaf\branch-sales-management\dist\assets\*.*" "c:\Users\prart\OneDrive\Desktop\BananaLeaf\branch-sales-backend\src\main\resources\static\assets\"
copy /Y "c:\Users\prart\OneDrive\Desktop\BananaLeaf\branch-sales-management\dist\*.*" "c:\Users\prart\OneDrive\Desktop\BananaLeaf\branch-sales-backend\src\main\resources\static\"
echo DONE COPYING
dir "c:\Users\prart\OneDrive\Desktop\BananaLeaf\branch-sales-backend\src\main\resources\static"
