package com.gamebuster19901.excite.bot.graphics.dice.animation;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

@Deprecated
public class GifWriter implements Closeable {

	protected ImageWriter writer;
	protected ImageWriteParam params;
	protected IIOMetadata metaData;
	ImageOutputStream stream;
	
	public GifWriter(ImageOutputStream out, int imageType, int delay, int loopCount) throws IOException {
		//this(out, imageType, delay, (char)loopCount);
	}
	
    public GifWriter(ByteArrayOutputStream out, int imageType, int delay, boolean loop) throws IOException {
        writer = ImageIO.getImageWritersBySuffix("gif").next();
        params = writer.getDefaultWriteParam();

        ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
        metaData = writer.getDefaultImageMetadata(imageTypeSpecifier, params);

        configureRootMetadata(delay, loop);
        stream = ImageIO.createImageOutputStream(out);
        writer.setOutput(stream);
        writer.prepareWriteSequence(null);
    }
	
    private void configureRootMetadata(int delay, boolean loop) throws IIOInvalidTreeException {
        String metaFormatName = metaData.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) metaData.getAsTree(metaFormatName);

        IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delay / 10));
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

        IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
        commentsNode.setAttribute("CommentExtension", "Created by: https://memorynotfound.com");

        IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");

        int loopContinuously = loop ? 0 : 1;
        child.setUserObject(new byte[]{ 0x1, (byte) (loopContinuously & 0xFF), (byte) ((loopContinuously >> 8) & 0xFF)});
        appExtensionsNode.appendChild(child);
        metaData.setFromTree(metaFormatName, root);
    }
	
	private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName){
		int nNodes = rootNode.getLength();
		for (int i = 0; i < nNodes; i++){
			if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)){
				return (IIOMetadataNode) rootNode.item(i);
			}
		}
		IIOMetadataNode node = new IIOMetadataNode(nodeName);
		rootNode.appendChild(node);
		return(node);
	}
	
	public void write(RenderedImage img) throws IOException {
		writer.writeToSequence(new IIOImage(img, null, metaData), params);
	}
	
	public void close() throws IOException {
		writer.endWriteSequence();
		stream.close();
	}
}