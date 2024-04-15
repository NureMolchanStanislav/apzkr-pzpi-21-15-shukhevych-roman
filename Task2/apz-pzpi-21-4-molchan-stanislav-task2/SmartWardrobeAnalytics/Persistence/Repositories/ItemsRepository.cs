using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class ItemsRepository(MongoDbContext db) : BaseRepository<Item>(db, "Items"), IItemsRepository 
{
    
}