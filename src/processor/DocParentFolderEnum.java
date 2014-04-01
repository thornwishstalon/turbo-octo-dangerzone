package processor;

public enum DocParentFolderEnum {
	ALT_ATHEISM(1), COMP_GRAPHICS(2), COMP_OS_MS_WINDOWS_MISC(3), COMP_SYS_IBM_PC_HARDWARE(
			4), COMP_SYS_MAC_HARDWARE(5), COMP_WINDOWS_X(6), MISC_FORSALE(7), REC_AUTOS(
			8), REC_MOTORCYCLES(9), REC_SPORT_BASEBALL(10), REC_SPORT_HOCKEY(11), SCI_CRYPT(
			12), SCI_ELECTRONICS(13), SCI_MED(14), SCI_SPACE(15), SOC_RELIGION_CHRISTIAN(
			16), TALK_POLITICS_GUNS(17), TALK_POLITICS_MIDEAST(18), TALK_POLITICS_MISC(
			19), TALK_RELIGION_MISC(20);

	private int value;

	private DocParentFolderEnum(int value) {
		this.value = value;
	}

	/*
	 * public int getValue() { return value; }
	 */

	public int getValue(String name) {
		if (ALT_ATHEISM.name().equals(name))
			return value;
		else if (COMP_GRAPHICS.name().equals(name))
			return value;
		else if (COMP_OS_MS_WINDOWS_MISC.name().equals(name))
			return value;
		else if (COMP_SYS_IBM_PC_HARDWARE.name().equals(name))
			return value;
		else if (COMP_SYS_MAC_HARDWARE.name().equals(name))
			return value;
		else if (COMP_WINDOWS_X.name().equals(name))
			return value;
		else if (MISC_FORSALE.name().equals(name))
			return value;
		else if (REC_AUTOS.name().equals(name))
			return value;
		else if (REC_MOTORCYCLES.name().equals(name))
			return value;
		else if (REC_SPORT_BASEBALL.name().equals(name))
			return value;
		else if (REC_SPORT_HOCKEY.name().equals(name))
			return value;
		else if (SCI_CRYPT.name().equals(name))
			return value;
		else if (SCI_ELECTRONICS.name().equals(name))
			return value;
		else if (SCI_MED.name().equals(name))
			return value;
		else if (SCI_SPACE.name().equals(name))
			return value;
		else if (SOC_RELIGION_CHRISTIAN.name().equals(name))
			return value;
		else if (TALK_POLITICS_GUNS.name().equals(name))
			return value;
		else if (TALK_POLITICS_MIDEAST.name().equals(name))
			return value;
		else if (TALK_POLITICS_MISC.name().equals(name))
			return value;
		else if (TALK_RELIGION_MISC.name().equals(name))
			return value;
		else
			return 0;
	}

	// public <E extends Enum<E>> E getEnumValue(Class<E> clazz, String name) {
	// E e = Enum.valueOf(clazz, name);
	// return e;
	// }
}
