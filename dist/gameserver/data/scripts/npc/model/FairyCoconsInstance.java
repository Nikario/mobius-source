/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package npc.model;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.GameTimeController;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author ALF
 * @date 03.09.2012
 */
public final class FairyCoconsInstance extends NpcInstance
{
	private static final int[] little_simpleAttackMobs =
	{
		22863,
		22871,
		22879,
		22887,
		22895,
		22903
	};
	private static final int[] big_simpleAttackMobs =
	{
		22867,
		22875,
		22883,
		22891,
		22899,
		22907
	};
	private static final int[] little_skillAttackMobs =
	{
		22866,
		22874,
		22882,
		22890,
		22898,
		22906
	};
	private static final int[] big_skillAttackMobs =
	{
		22870,
		22878,
		22886,
		22894,
		22898,
		22910
	};
	private static final int[] little_nightAttackMobs =
	{
		22864,
		22872,
		22880,
		22888,
		22896,
		22904
	};
	private static final int[] big_nightAttackMobs =
	{
		22868,
		22876,
		22884,
		22892,
		22900,
		22908
	};
	
	public FairyCoconsInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public boolean isMovementDisabled()
	{
		return true;
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		if (!GameTimeController.getInstance().isNowNight())
		{
			if (command.equals("1"))
			{
				int npdId = getId();
				
				switch (npdId)
				{
					case 32919:
					{
						NpcInstance ni = getReflection().addSpawnWithoutRespawn(little_simpleAttackMobs[Rnd.get(little_simpleAttackMobs.length)], getLoc(), 150);
						ni.getAI().Attack(player, true, false);
						decayMe();
						doDie(player);
						break;
					}
					
					case 32920:
					{
						for (int i = 0; i < 3; i++)
						{
							NpcInstance ni = getReflection().addSpawnWithoutRespawn(big_simpleAttackMobs[Rnd.get(big_simpleAttackMobs.length)], getLoc(), 150);
							ni.getAI().Attack(player, true, false);
						}
						
						decayMe();
						doDie(player);
						break;
					}
				}
			}
			else if (command.equals("2"))
			{
				int npdId = getId();
				
				switch (npdId)
				{
					case 32919:
					{
						NpcInstance ni = getReflection().addSpawnWithoutRespawn(little_skillAttackMobs[Rnd.get(little_skillAttackMobs.length)], getLoc(), 150);
						ni.getAI().Attack(player, true, false);
						decayMe();
						doDie(player);
						break;
					}
					
					case 32920:
					{
						for (int i = 0; i < 3; i++)
						{
							NpcInstance ni = getReflection().addSpawnWithoutRespawn(big_skillAttackMobs[Rnd.get(big_skillAttackMobs.length)], getLoc(), 150);
							ni.getAI().Attack(player, true, false);
						}
						
						decayMe();
						doDie(player);
						break;
					}
				}
			}
		}
		else
		{
			int npdId = getId();
			
			switch (npdId)
			{
				case 32919:
				{
					NpcInstance ni = getReflection().addSpawnWithoutRespawn(little_nightAttackMobs[Rnd.get(little_nightAttackMobs.length)], getLoc(), 150);
					ni.getAI().Attack(player, true, false);
					decayMe();
					doDie(player);
					break;
				}
				
				case 32920:
				{
					for (int i = 0; i < 3; i++)
					{
						NpcInstance ni = getReflection().addSpawnWithoutRespawn(big_nightAttackMobs[Rnd.get(big_nightAttackMobs.length)], getLoc(), 150);
						ni.getAI().Attack(player, true, false);
					}
					
					doDie(player);
					decayMe();
					break;
				}
			}
		}
	}
	
	@Override
	protected void onReduceCurrentHp(double damage, Creature attacker, Skill skill, boolean awake, boolean standUp, boolean directHp)
	{
		int npdId = getId();
		
		if (!GameTimeController.getInstance().isNowNight())
		{
			if (npdId == 32919)
			{
				if (skill == null)
				{
					NpcInstance ni = getReflection().addSpawnWithoutRespawn(little_simpleAttackMobs[Rnd.get(little_simpleAttackMobs.length)], getLoc(), 150);
					ni.getAI().Attack(attacker, true, false);
					decayMe();
					doDie(attacker);
				}
				else
				{
					NpcInstance ni = getReflection().addSpawnWithoutRespawn(little_skillAttackMobs[Rnd.get(little_skillAttackMobs.length)], getLoc(), 150);
					ni.getAI().Attack(attacker, true, false);
					decayMe();
					doDie(attacker);
				}
			}
			else if (skill == null)
			{
				for (int i = 0; i < 3; i++)
				{
					NpcInstance ni = getReflection().addSpawnWithoutRespawn(big_simpleAttackMobs[Rnd.get(big_simpleAttackMobs.length)], getLoc(), 150);
					ni.getAI().Attack(attacker, true, false);
				}
				
				decayMe();
				doDie(attacker);
			}
			else
			{
				for (int i = 0; i < 3; i++)
				{
					NpcInstance ni = getReflection().addSpawnWithoutRespawn(big_skillAttackMobs[Rnd.get(big_skillAttackMobs.length)], getLoc(), 150);
					ni.getAI().Attack(attacker, true, false);
				}
				
				decayMe();
				doDie(attacker);
			}
		}
		else if (npdId == 32919)
		{
			NpcInstance ni = getReflection().addSpawnWithoutRespawn(little_nightAttackMobs[Rnd.get(little_nightAttackMobs.length)], getLoc(), 150);
			ni.getAI().Attack(attacker, true, false);
			decayMe();
			doDie(attacker);
		}
		else
		{
			for (int i = 0; i < 3; i++)
			{
				NpcInstance ni = getReflection().addSpawnWithoutRespawn(big_nightAttackMobs[Rnd.get(big_nightAttackMobs.length)], getLoc(), 150);
				ni.getAI().Attack(attacker, true, false);
			}
			
			decayMe();
			doDie(attacker);
		}
	}
}