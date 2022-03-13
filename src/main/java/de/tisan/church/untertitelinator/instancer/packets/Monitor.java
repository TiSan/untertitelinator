package de.tisan.church.untertitelinator.instancer.packets;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Monitor {

	byte[] image;

	String name;

	int[] bounds;

	@JsonIgnore
	Image renderedImage;

	public Monitor() {
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getBounds() {
		return bounds;
	}

	public void setBounds(int[] bounds) {
		this.bounds = bounds;
	}

	public void preRender() {
		try {
			InputStream is = new ByteArrayInputStream(getImage());
			renderedImage = ImageIO.read(is).getScaledInstance(200, 100, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@JsonIgnore
	public Image getPrerenderedImage() {
		return renderedImage;
	}
}
