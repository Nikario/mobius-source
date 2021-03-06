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
package handlers.voicedcommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.dao.ItemsDAO;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.handlers.IVoicedCommandHandler;
import lineage2.gameserver.handlers.VoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Repair extends Functions implements IVoicedCommandHandler, ScriptFile
{
	private static final Logger _log = LoggerFactory.getLogger(Repair.class);
	private final String[] _commandList = new String[]
	{
		"repair"
	};
	
	/**
	 * Method getVoicedCommandList.
	 * @return String[]
	 * @see lineage2.gameserver.handlers.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
	
	/**
	 * Method useVoicedCommand.
	 * @param command String
	 * @param activeChar Player
	 * @param target String
	 * @return boolean
	 * @see lineage2.gameserver.handlers.IVoicedCommandHandler#useVoicedCommand(String, Player, String)
	 */
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (!target.isEmpty())
		{
			if (activeChar.getName().equals(target))
			{
				sendMessage("You cannot repair yourself.", activeChar);
				return false;
			}
			
			int objId = 0;
			
			for (Map.Entry<Integer, String> e : activeChar.getAccountChars().entrySet())
			{
				if (e.getValue().equals(target))
				{
					objId = e.getKey();
					break;
				}
			}
			
			if (objId == 0)
			{
				sendMessage("You can repair character only on same account.", activeChar);
				return false;
			}
			else if (World.getPlayer(objId) != null)
			{
				sendMessage("Character is still online.", activeChar);
				return false;
			}
			
			Connection con = null;
			PreparedStatement statement = null;
			PreparedStatement statement2 = null;
			ResultSet rs = null;
			
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("SELECT karma FROM characters WHERE obj_Id=?");
				statement.setInt(1, objId);
				statement.execute();
				rs = statement.getResultSet();
				int karma = 0;
				rs.next();
				karma = rs.getInt("karma");
				DbUtils.close(statement, rs);
				
				if (karma > 0)
				{
					statement = con.prepareStatement("UPDATE characters SET x=17144, y=170156, z=-3502 WHERE obj_Id=?");
					statement.setInt(1, objId);
					statement.execute();
					DbUtils.close(statement);
				}
				else
				{
					statement2 = con.prepareStatement("UPDATE characters SET x=0, y=0, z=0 WHERE obj_Id=?");
					statement2.setInt(1, objId);
					statement2.execute();
					DbUtils.close(statement2);
					Collection<ItemInstance> items = ItemsDAO.getInstance().getItemsByOwnerIdAndLoc(objId, ItemLocation.PAPERDOLL);
					
					for (ItemInstance item : items)
					{
						item.setEquipped(false);
						item.setLocData(0);
						item.setLocation(ItemLocation.WAREHOUSE);
						item.setJdbcState(JdbcEntityState.UPDATED);
						item.update();
					}
				}
				
				statement = con.prepareStatement("DELETE FROM character_variables WHERE obj_id=? AND type='user-var' AND name='reflection'");
				statement.setInt(1, objId);
				statement.execute();
				DbUtils.close(statement);
				sendMessage("Character successfully repaired.", activeChar);
				return true;
			}
			catch (Exception e)
			{
				_log.error("", e);
				return false;
			}
			finally
			{
				DbUtils.closeQuietly(con, statement, rs);
			}
		}
		
		activeChar.sendMessage(".repair <name>");
		return false;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
}
