package com.ebupt.portal.canyon.common.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.StoppedSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.util.CollectionUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;

/**
 * 自定义Shiro管理的session
 *
 * @author chy
 * @date 2019-03-17 14:45
 */
@Slf4j
public class ShiroSession implements ValidatingSession, Serializable {

	private static final long serialVersionUID = -7125642695178165650L;

	protected static final long MILLIS_PER_SECOND = 1000;
	protected static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	protected static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

	static int bitIndexCounter = 0;
	private static final int ID_BIT_MASK = 1 << bitIndexCounter++;
	private static final int START_TIMESTAMP_BIT_MASK = 1 << bitIndexCounter++;
	private static final int STOP_TIMESTAMP_BIT_MASK = 1 << bitIndexCounter++;
	private static final int LAST_ACCESS_TIME_BIT_MASK = 1 << bitIndexCounter++;
	private static final int TIMEOUT_BIT_MASK = 1 << bitIndexCounter++;
	private static final int EXPIRED_BIT_MASK = 1 << bitIndexCounter++;
	private static final int HOST_BIT_MASK = 1 << bitIndexCounter++;
	private static final int ATTRIBUTES_BIT_MASK = 1 << bitIndexCounter++;

	private Serializable id;
	private Date startTimestamp;
	private Date stopTimestamp;
	private Date lastAccessTime;
	private long timeout;
	private boolean expired;
	private String host;
	private Map<Object, Object> attributes;
	private transient boolean isChanged = false;

	public ShiroSession() {
		this.timeout = DefaultSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT;
		this.startTimestamp = new Date();
		this.lastAccessTime = this.startTimestamp;
		this.setChanged(true);
	}

	public ShiroSession(String host) {
		this();
		this.host = host;
		this.setChanged(true);
	}

	@Override
	public Serializable getId() {
		return this.id;
	}

	public void setId(Serializable id) {
		this.id = id;
		this.setChanged(true);
	}

	@Override
	public Date getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
		this.setChanged(true);
	}

	public Date getStopTimestamp() {
		return stopTimestamp;
	}

	public void setStopTimestamp(Date stopTimestamp) {
		this.stopTimestamp = stopTimestamp;
		this.setChanged(true);
	}

	@Override
	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
		this.setChanged(true);
	}

	@Override
	public long getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(long timeout) {
		this.timeout = timeout;
		this.setChanged(true);
	}

	@Override
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		this.setChanged(true);
	}

	public Map<Object, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<Object, Object> attributes) {
		this.attributes = attributes;
		this.setChanged(true);
	}

	@Override
	public void touch() {
		this.lastAccessTime = new Date();
	}

	@Override
	public void stop() {
		if (this.stopTimestamp == null) {
			this.stopTimestamp = new Date();
			this.setChanged(true);
		}
	}

	protected boolean isStopped() {
		return getStopTimestamp() != null;
	}

	protected void expire() {
		stop();
		this.expired = true;
		this.setChanged(true);
	}

	/**
	 * @since 0.9
	 */
	@Override
	public boolean isValid() {
		return !isStopped() && !isExpired();
	}

	protected boolean isTimedOut() {

		if (isExpired()) {
			return true;
		}

		long timeout = getTimeout();

		if (timeout >= 0L) {

			Date lastAccessTime = getLastAccessTime();

			if (lastAccessTime == null) {
				String msg = "session.lastAccessTime for session with id [" +
						getId() + "] is null.  This value must be set at " +
						"least once, preferably at least upon instantiation.  Please check the " +
						getClass().getName() + " implementation and ensure " +
						"this value will be set (perhaps in the constructor?)";
				throw new IllegalStateException(msg);
			}

			long expireTimeMillis = System.currentTimeMillis() - timeout;
			Date expireTime = new Date(expireTimeMillis);
			return lastAccessTime.before(expireTime);
		} else {
			if (log.isTraceEnabled()) {
				log.trace("No timeout for session with id [" + getId() +
						"].  Session is not considered expired.");
			}
		}

		return false;
	}

	@Override
	public void validate() throws InvalidSessionException {
		if (isStopped()) {
			String msg = "Session with id [" + getId() + "] has been " +
					"explicitly stopped.  No further interaction under this session is " +
					"allowed.";
			throw new StoppedSessionException(msg);
		}

		if (isTimedOut()) {
			expire();

			Date lastAccessTime = getLastAccessTime();
			long timeout = getTimeout();

			Serializable sessionId = getId();

			DateFormat df = DateFormat.getInstance();
			String msg = "Session with id [" + sessionId + "] has expired. " +
					"Last access time: " + df.format(lastAccessTime) +
					".  Current time: " + df.format(new Date()) +
					".  Session timeout is set to " + timeout / MILLIS_PER_SECOND + " seconds (" +
					timeout / MILLIS_PER_MINUTE + " minutes)";
			if (log.isTraceEnabled()) {
				log.trace(msg);
			}
			throw new ExpiredSessionException(msg);
		}
	}

	private Map<Object, Object> getAttributesLazy() {
		Map<Object, Object> attributes = getAttributes();
		if (attributes == null) {
			attributes = new HashMap<>(10);
			setAttributes(attributes);
		}
		return attributes;
	}

	@Override
	public Collection<Object> getAttributeKeys() throws InvalidSessionException {
		Map<Object, Object> attributes = getAttributes();
		if (attributes == null) {
			return Collections.emptySet();
		}
		return attributes.keySet();
	}

	@Override
	public Object getAttribute(Object key) {
		Map<Object, Object> attributes = getAttributes();
		if (attributes == null) {
			return null;
		}
		return attributes.get(key);
	}

	@Override
	public void setAttribute(Object key, Object value) {
		if (value == null) {
			removeAttribute(key);
		} else {
			getAttributesLazy().put(key, value);
		}
		this.setChanged(true);
	}

	@Override
	public Object removeAttribute(Object key) {
		Map<Object, Object> attributes = getAttributes();
		if (attributes == null) {
			return null;
		} else {
			this.setChanged(true);
			return attributes.remove(key);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof SimpleSession) {
			SimpleSession other = (SimpleSession) obj;
			Serializable thisId = getId();
			Serializable otherId = other.getId();
			if (thisId != null && otherId != null) {
				return thisId.equals(otherId);
			} else {
				return onEquals(other);
			}
		}
		return false;
	}

	protected boolean onEquals(SimpleSession ss) {
		return (getStartTimestamp() != null ? getStartTimestamp().equals(ss.getStartTimestamp()) : ss.getStartTimestamp() == null) &&
				(getStopTimestamp() != null ? getStopTimestamp().equals(ss.getStopTimestamp()) : ss.getStopTimestamp() == null) &&
				(getLastAccessTime() != null ? getLastAccessTime().equals(ss.getLastAccessTime()) : ss.getLastAccessTime() == null) &&
				(getTimeout() == ss.getTimeout()) &&
				(isExpired() == ss.isExpired()) &&
				(getHost() != null ? getHost().equals(ss.getHost()) : ss.getHost() == null) &&
				(getAttributes() != null ? getAttributes().equals(ss.getAttributes()) : ss.getAttributes() == null);
	}

	@Override
	public int hashCode() {
		Serializable id = getId();
		if (id != null) {
			return id.hashCode();
		}
		int hashCode = getStartTimestamp() != null ? getStartTimestamp().hashCode() : 0;
		hashCode = 31 * hashCode + (getStopTimestamp() != null ? getStopTimestamp().hashCode() : 0);
		hashCode = 31 * hashCode + (getLastAccessTime() != null ? getLastAccessTime().hashCode() : 0);
		hashCode = 31 * hashCode + Long.valueOf(Math.max(getTimeout(), 0)).hashCode();
		hashCode = 31 * hashCode + Boolean.valueOf(isExpired()).hashCode();
		hashCode = 31 * hashCode + (getHost() != null ? getHost().hashCode() : 0);
		hashCode = 31 * hashCode + (getAttributes() != null ? getAttributes().hashCode() : 0);
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName()).append(",id=").append(getId());
		return sb.toString();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		short alteredFieldsBitMask = getAlteredFieldsBitMask();
		out.writeShort(alteredFieldsBitMask);
		if (id != null) {
			out.writeObject(id);
		}
		if (startTimestamp != null) {
			out.writeObject(startTimestamp);
		}
		if (stopTimestamp != null) {
			out.writeObject(stopTimestamp);
		}
		if (lastAccessTime != null) {
			out.writeObject(lastAccessTime);
		}
		if (timeout != 0L) {
			out.writeLong(timeout);
		}
		if (expired) {
			out.writeBoolean(expired);
		}
		if (host != null) {
			out.writeUTF(host);
		}
		if (!CollectionUtils.isEmpty(attributes)) {
			out.writeObject(attributes);
		}
	}

	@SuppressWarnings({"unchecked"})
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		short bitMask = in.readShort();

		if (isFieldPresent(bitMask, ID_BIT_MASK)) {
			this.id = (Serializable) in.readObject();
		}
		if (isFieldPresent(bitMask, START_TIMESTAMP_BIT_MASK)) {
			this.startTimestamp = (Date) in.readObject();
		}
		if (isFieldPresent(bitMask, STOP_TIMESTAMP_BIT_MASK)) {
			this.stopTimestamp = (Date) in.readObject();
		}
		if (isFieldPresent(bitMask, LAST_ACCESS_TIME_BIT_MASK)) {
			this.lastAccessTime = (Date) in.readObject();
		}
		if (isFieldPresent(bitMask, TIMEOUT_BIT_MASK)) {
			this.timeout = in.readLong();
		}
		if (isFieldPresent(bitMask, EXPIRED_BIT_MASK)) {
			this.expired = in.readBoolean();
		}
		if (isFieldPresent(bitMask, HOST_BIT_MASK)) {
			this.host = in.readUTF();
		}
		if (isFieldPresent(bitMask, ATTRIBUTES_BIT_MASK)) {
			this.attributes = (Map<Object, Object>) in.readObject();
		}
	}

	private short getAlteredFieldsBitMask() {
		int bitMask = 0;
		bitMask = id != null ? bitMask | ID_BIT_MASK : bitMask;
		bitMask = startTimestamp != null ? bitMask | START_TIMESTAMP_BIT_MASK : bitMask;
		bitMask = stopTimestamp != null ? bitMask | STOP_TIMESTAMP_BIT_MASK : bitMask;
		bitMask = lastAccessTime != null ? bitMask | LAST_ACCESS_TIME_BIT_MASK : bitMask;
		bitMask = timeout != 0L ? bitMask | TIMEOUT_BIT_MASK : bitMask;
		bitMask = expired ? bitMask | EXPIRED_BIT_MASK : bitMask;
		bitMask = host != null ? bitMask | HOST_BIT_MASK : bitMask;
		bitMask = !CollectionUtils.isEmpty(attributes) ? bitMask | ATTRIBUTES_BIT_MASK : bitMask;
		return (short) bitMask;
	}

	private static boolean isFieldPresent(short bitMask, int fieldBitMask) {
		return (bitMask & fieldBitMask) != 0;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean changed) {
		isChanged = changed;
	}
}
