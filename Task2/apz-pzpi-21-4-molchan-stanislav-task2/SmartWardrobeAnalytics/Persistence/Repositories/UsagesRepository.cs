using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class UsagesRepository(MongoDbContext db) : BaseRepository<Usages>(db, "Usages"), IUsagesRepository
{
    
}