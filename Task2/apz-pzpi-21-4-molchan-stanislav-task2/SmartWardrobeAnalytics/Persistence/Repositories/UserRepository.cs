using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class UsersRepository(MongoDbContext db) : BaseRepository<User>(db, "Users"), IUsersRepository
{
    
}