import javax.jcr.Session
import org.apache.sling.api.resource.ModifiableValueMap
import org.apache.sling.api.resource.PersistenceException
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver

// ==== CONFIGURE ROOT PATH UNDER WHICH SERACH FOR COMPONENT IS INITIATED ====
def rootPath           = "/content/wknd"

// The sling:resourceType to match (e.g. "wknd/components/page")
def targetResourceType = "wknd/components/customcomponent"

// The property name you wish to update
def propertyName       = "customPropertyName"

// The new value to set
def newValue           = "customPropertyValue"
// ======================

// resolve the root
ResourceResolver resolver = resourceResolver
Resource root = resolver.getResource(rootPath)
if (root == null) {
    println "ERROR: Could not resolve ${rootPath}"
    return
}

// adapt to JCR Session for saving later
Session session = resolver.adaptTo(Session)
if (session == null) {
    println "ERROR: Could not get JCR Session from ResourceResolver"
    return
}

def updatedCount = 0

/**
 * Recursively traverse and update matching resources.
 */
def traverseAndUpdate
traverseAndUpdate = { Resource res ->
    // check resourceType
    if (res.resourceType == targetResourceType) {
        ModifiableValueMap props = res.adaptTo(ModifiableValueMap)
        if (props) {
            props.put(propertyName, newValue)
            println "Updated: ${res.path}/${propertyName} → ${newValue}"
            updatedCount++
        }
    }
    // recurse into children
    res.listChildren().each { traverseAndUpdate(it) }
}

// run the traversal
traverseAndUpdate(root)

if (updatedCount > 0) {
    try {
        session.save()
        println "\nSaved ${updatedCount} change(s) to the repository."
    } catch (PersistenceException e) {
        println "ERROR saving changes: ${e.message}"
    }
} else {
    println "\nNo matching resources found under ${rootPath} with sling:resourceType='${targetResourceType}'."
}
