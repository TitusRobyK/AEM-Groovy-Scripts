import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ValueMap


def rootPath = "/content/wknd/us/en"  

Resource root = resourceResolver.getResource(rootPath)
if (root == null) {
    println "ERROR: Path not found: ${rootPath}"
    return
}


def images = [] as Set<String>


def traverse
traverse = { Resource res ->

    ValueMap props = res.adaptTo(ValueMap)
    if (props) {
        def fileRef = props.get("fileReference", String)
        if (fileRef) {
            images << fileRef
        }

        def src = props.get("src", String)
        if (src) {
            images << src
        }

        ["imagePath","poster","thumbnail"].each { key ->
            def v = props.get(key, String)
            if (v) { images << v }
        }
    }
    res.listChildren().each { traverse(it) }
}


traverse(root)
println "\nFound ${images.size()} unique image references under ${rootPath}:\n"
images.sort().each { println it }
