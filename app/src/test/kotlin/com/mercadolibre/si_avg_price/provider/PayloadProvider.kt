package com.mercadolibre.si_avg_price.provider

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class PayloadProvider(file: String) {

  private var rootNode: ObjectNode
  private val mapper = jacksonObjectMapper()

  init {
    mapper.setSerializationInclusion(JsonInclude.Include.CUSTOM)
    val fileBytes = this::class.java.getResource(file)?.readBytes()

    rootNode = mapper.readTree(fileBytes) as ObjectNode
  }

  fun provideAsString(nodeToRemove: String? = null): String {
    val rootNodeCopy = rootNode.deepCopy()
    nodeToRemove?.let { removeNode(rootNodeCopy, it) }
    return rootNodeCopy.toPrettyString()
  }


  fun replace(nodeName: String, newValue: String): PayloadProvider {
    updateNode(rootNode, nodeName, newValue)
    return this
  }


  private fun removeNode(node: ObjectNode, nodeToRemove: String) {
    if (nodeToRemove.contains(".")) {
      val (currentNode, childNode) = getNodeAndChildNode(nodeToRemove, node)
      removeNode(currentNode, childNode)
    } else {
      node.remove(nodeToRemove)
    }
  }

  private fun updateNode(node: ObjectNode, nodeToReplace: String, newValue: String) {
    if (nodeToReplace.contains(".")) {
      val (currentNode, childNode) = getNodeAndChildNode(nodeToReplace, node)
      updateNode(currentNode, childNode, newValue)
    } else {
      node.replace(nodeToReplace, TextNode.valueOf(newValue))
    }
  }

  private fun getNodeAndChildNode(nodeToReplace: String, node: ObjectNode): Pair<ObjectNode, String> {
    val rootNode = nodeToReplace.split(".").first()
    val arrayRegex = ".*\\[([0-9]+)]".toRegex()

    val nodeResult = if (rootNode.matches(arrayRegex)) {
      val index = arrayRegex.find(rootNode)!!.groupValues[1]
      node.get(rootNode.replace("[$index]", ""))[index.toInt()] as ObjectNode
    } else
      node.get(rootNode) as ObjectNode

    val childNode = nodeToReplace.replace("$rootNode.", "")
    return Pair(nodeResult, childNode)
  }
}
