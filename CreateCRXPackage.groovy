/* 
In the Data Section , add the paths as shown below
/content/abc
/content/xyz
/content/qwerty/mno/pwse
Upon execution of the following code , This would create a package called "Sample-Package" with the above paths as filters.
*/


import com.adobe.acs.commons.packaging.PackageHelper.ConflictResolution
import java.text.SimpleDateFormat

String packageGroup ="package-group";
String packageName ="Sample-Package";

String[] paths = data.split("\n");
List<String> inputPathList = Arrays.asList(paths);

def packageHelper = getService("com.adobe.acs.commons.packaging.PackageHelper")
packageHelper.createPackageForPaths(
    inputPathList ?: [],
    session,
    packageGroup,
    packageName,
    null ?: new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
    ConflictResolution.valueOf(null ?: "None"),
    null ?: [:])
