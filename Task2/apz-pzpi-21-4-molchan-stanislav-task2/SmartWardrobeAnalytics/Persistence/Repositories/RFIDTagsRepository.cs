using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class RFIDTagsRepository(MongoDbContext db) : BaseRepository<RFIDTag>(db, "RFIDTags"), IRFIDTagsRepository
{
    
}